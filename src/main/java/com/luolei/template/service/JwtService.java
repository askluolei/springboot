package com.luolei.template.service;

import com.luolei.template.domain.Token;
import com.luolei.template.domain.support.AuthType;
import com.luolei.template.domain.support.RequestPlatform;
import com.luolei.template.error.BizException;
import com.luolei.template.repository.TokenRepository;
import com.luolei.template.security.SecurityUtils;
import com.luolei.template.security.jwt.TokenProvider;
import com.luolei.template.service.mapper.TokenMapper;
import com.luolei.template.support.Constants;
import com.luolei.template.support.R;
import com.luolei.template.utils.Sequence;
import com.luolei.template.web.dto.TokenDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.cache.Cache;
import javax.cache.CacheManager;
import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

/**
 * 认证 service
 * @author luolei
 * @createTime 2018-04-03 23:44
 */
@Slf4j
@Service
public class JwtService {

    private final TokenProvider tokenProvider;
    private final AuthenticationManager authenticationManager;
    private final Sequence sequence;
    private final TokenRepository tokenRepository;
    private final TokenMapper tokenMapper;
    private final Cache<String, String> tokenCache;

    public JwtService(TokenProvider tokenProvider, AuthenticationManager authenticationManager, Sequence sequence, TokenRepository tokenRepository, TokenMapper tokenMapper, CacheManager cacheManager, PasswordEncoder passwordEncoder) {
        this.tokenProvider = tokenProvider;
        this.authenticationManager = authenticationManager;
        this.sequence = sequence;
        this.tokenRepository = tokenRepository;
        this.tokenMapper = tokenMapper;
        this.tokenCache = cacheManager.getCache(Constants.CACHE_TOKEN_INVALID);
    }

    /**
     * 使用refreshToken 换取凭证
     * @param refreshToken
     * @param platform
     * @param ip
     * @return
     */
    public TokenDto authorize(String refreshToken, RequestPlatform platform, String ip) {
        boolean isValid = tokenProvider.validateToken(refreshToken, TokenProvider.REFRESH_TOKEN_TYPE);
        if (!isValid) {
            throw new BizException("不合法的凭证").withCode(R.LOGIN_ERROR);
        }
        long random = tokenProvider.getRamdom(refreshToken);
        String username = tokenProvider.getUsername(refreshToken);
        Integer countByRandom = tokenRepository.countByRandom(random);
        if (Objects.isNull(countByRandom) || countByRandom == 0) {
            throw new BizException("可能被踢出了").withCode(R.LOGIN_ERROR);
        }
        /**
         * 防止重复生成token，如果之前的token 有效期还没过一半，就直接返回这个
         * 有个问题，就是，如果用户的权限做了修改，那token里面的信息没更新
         * 要想获取最新权限，必须得重新认证，这也是很正常的情况，当使用refreshToken 换凭证的时候，当时认证的权限是咋样的，那就是咋样的
         */
        Optional<Token> optional = tokenRepository.findFirstByUsernameAndLoginIpAndPlatformAndAuthTypeAndExpireTimeAfterOrderByExpireTimeDesc(username, ip, platform, AuthType.REFRESH_TOKEN, Instant.now().plusMillis(this.tokenProvider.getTokenValidityInMilliseconds() / 2));
        if (optional.isPresent()) {
            TokenDto tokenDto = tokenMapper.fromToken(optional.get());
            tokenDto.setOnlineCount(tokenRepository.countAllByExpireTimeAfterAndUsernameEquals(Instant.now(), username));
            return tokenDto;
        }
        /**
         * 这里的权限，还是使用 以前的权限，如果有修改，是看不到的
         */
        Authentication authentication = tokenProvider.getAuthentication(refreshToken);
        Token token = new Token();
        token.setLoginIp(ip);
        token.setAuthType(AuthType.REFRESH_TOKEN);
        token.setPlatform(platform);
        token.setRandom(random);
        token.setExpireTime(Instant.now().plusMillis(tokenProvider.getTokenValidityInMilliseconds()));
        token.setAccessToken(tokenProvider.createAccessToken(authentication, random));
        token.setRefreshToken(refreshToken);

        TokenDto tokenDto = tokenMapper.fromToken(token);
        tokenDto.setOnlineCount(tokenRepository.countAllByExpireTimeAfterAndUsernameEquals(Instant.now(), username));

        /**
         * 这里refreshToken 不返回，减少refreshToken 在网络中的传输次数
         */
        tokenDto.setRefreshToken(null);
        return tokenDto;
    }

    /**
     * 认证
     * @param username
     * @param password
     * @param rememberMe
     * @return
     */
    public TokenDto authorize(String username, String password, boolean rememberMe, RequestPlatform platform, String ip) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        Authentication authentication = this.authenticationManager.authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        Optional<Token> optional = tokenRepository.findFirstByUsernameAndLoginIpAndPlatformAndAuthTypeAndExpireTimeAfterOrderByExpireTimeDesc(username, ip, platform, AuthType.USER_PASS, Instant.now());
        Token token = null;
        /**
         * 相同帐号密码，相同ip，相同请求平台
         * 不在重复生成token
         */
        if (optional.isPresent()) {
            token = optional.get();
        } else {
            token = new Token();
            token.setLoginIp(ip);
            token.setAuthType(AuthType.USER_PASS);
            token.setPlatform(platform);
            token.setUsername(username);
            long random = sequence.nextId();
            token.setRandom(random);
            String accessToken = tokenProvider.createAccessToken(authentication, random);
            token.setExpireTime(Instant.now().plusMillis(tokenProvider.getTokenValidityInMilliseconds()));
            log.debug("accessToken:{}", accessToken);
            token.setAccessToken(accessToken);
            if (rememberMe) {
                String refreshToken = tokenProvider.createRefreshToken(authentication, random);
                log.debug("refreshToken:{}", refreshToken);
                token.setRefreshToken(refreshToken);
            }
            token = tokenRepository.save(token);
        }
        TokenDto tokenDto = tokenMapper.fromToken(token);
        String tempToken = tokenProvider.createTempToken(Constants.OFFLINE_EXPIRED_SECOND);
        tokenDto.setOfflineToken(tempToken);
        tokenDto.setOnlineCount(tokenRepository.countAllByExpireTimeAfterAndUsernameEquals(Instant.now(), username));
        return tokenDto;
    }

    /**
     * 踢出本帐号的其他在线
     * 注意，踢出操作不仅踢出当前在线，其他地方的记住密码也全部失效
     *
     * @param token
     * @param tempToken
     */
    @Transactional
    public TokenDto offline(String token, String tempToken) {
        if (!tokenProvider.validateToken(tempToken, TokenProvider.TEMP_TOKEN_TYPE)) {
            throw new BizException("踢人凭证无效，无法执行踢人操作");
        }
        String username = SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new BizException("无法获取当前登录的用户名"));
        long random = tokenProvider.getRamdom(token);
        tokenRepository.deleteByUsernameAndRandomNot(username, random);
        TokenDto tokenDto = new TokenDto();
        tokenDto.setOnlineCount(tokenRepository.countAllByExpireTimeAfterAndUsernameEquals(Instant.now(), username));
        tokenCache.put(username, token);
        return tokenDto;
    }
}
