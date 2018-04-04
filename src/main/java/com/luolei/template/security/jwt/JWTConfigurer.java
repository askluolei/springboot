package com.luolei.template.security.jwt;

import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.cache.Cache;

/**
 * @author 罗雷
 * @date 2018/4/2 0002
 * @time 16:24
 */
public class JWTConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    public static final String AUTHORIZATION_HEADER = "Authorization";

    public static final String AUTHORIZATION_TOKEN = "access_token";

    private TokenProvider tokenProvider;
    Cache<String, String> tokenCache;

    public JWTConfigurer(TokenProvider tokenProvider, Cache<String, String> tokenCache) {
        this.tokenProvider = tokenProvider;
        this.tokenCache = tokenCache;
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        JWTFilter customFilter = new JWTFilter(tokenProvider, tokenCache);
        http.addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
