package com.luolei.template.service;

import com.luolei.template.domain.Token;
import com.luolei.template.domain.support.AuthType;
import com.luolei.template.domain.support.RequestPlatform;
import com.luolei.template.repository.TokenRepository;
import com.luolei.template.security.jwt.TokenProvider;
import com.luolei.template.service.mapper.TokenMapper;
import com.luolei.template.utils.Sequence;
import com.luolei.template.web.dto.TokenDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

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

    public JwtService(TokenProvider tokenProvider, AuthenticationManager authenticationManager, Sequence sequence, TokenRepository tokenRepository, TokenMapper tokenMapper) {
        this.tokenProvider = tokenProvider;
        this.authenticationManager = authenticationManager;
        this.sequence = sequence;
        this.tokenRepository = tokenRepository;
        this.tokenMapper = tokenMapper;
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
        Token token = new Token();
        token.setLoginIP(ip);
        token.setAuthType(AuthType.USER_PASS);
        token.setPlatform(platform);
        token.setUsername(username);
        long random = sequence.nextId();
        token.setRandom(random);
        String accessToken = tokenProvider.createAccessToken(authentication, random);
        log.debug("accessToken:{}", accessToken);
        token.setAccessToken(accessToken);
        if (rememberMe) {
            String refreshToken = tokenProvider.createRefreshToken(authentication, random);
            log.debug("refreshToken:{}", refreshToken);
            token.setRefreshToken(refreshToken);
        }
        token = tokenRepository.save(token);
        return tokenMapper.fromToken(token);
    }
}
