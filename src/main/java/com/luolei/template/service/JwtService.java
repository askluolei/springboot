package com.luolei.template.service;

import com.luolei.template.domain.Token;
import com.luolei.template.domain.support.AuthType;
import com.luolei.template.domain.support.RequestPlatform;
import com.luolei.template.error.BaseException;
import com.luolei.template.repository.TokenRepository;
import com.luolei.template.security.SecurityUtils;
import com.luolei.template.security.jwt.TokenProvider;
import com.luolei.template.service.mapper.TokenMapper;
import com.luolei.template.support.Constants;
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
            throw new BaseException("踢人凭证无效，无法执行踢人操作");
        }
        String username = SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new BaseException("无法获取当前登录的用户名"));
        long random = tokenProvider.getRamdom(token);
        tokenRepository.deleteByUsernameAndRandomNot(username, random);
        TokenDto tokenDto = new TokenDto();
        tokenDto.setOnlineCount(tokenRepository.countAllByExpireTimeAfterAndUsernameEquals(Instant.now(), username));
        tokenCache.put(username, token);
        return tokenDto;
    }
}
