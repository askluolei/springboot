package com.luolei.template.security.jwt;

import com.alibaba.fastjson.JSON;
import com.luolei.template.error.BaseException;
import com.luolei.template.support.R;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.cache.Cache;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

import static com.luolei.template.security.jwt.TokenProvider.TOKEN_TYPE;
import static com.luolei.template.support.R.FORCE_OFFLINE;

/**
 * @author 罗雷
 * @date 2018/4/2 0002
 * @time 16:24
 */
public class JWTFilter extends GenericFilterBean {

    private TokenProvider tokenProvider;
    private Cache<String, String> tokenCache;

    public JWTFilter(TokenProvider tokenProvider, Cache<String, String> tokenCache) {
        this.tokenProvider = tokenProvider;
        this.tokenCache = tokenCache;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String jwt = resolveToken(httpServletRequest);
        if (StringUtils.hasText(jwt) && this.tokenProvider.validateToken(jwt, TOKEN_TYPE)) {
            Authentication authentication = this.tokenProvider.getAuthentication(jwt);
            String username = tokenProvider.getUsername(jwt);
            /**
             * 这里，如果有用户使用踢出下线功能，则会在缓存记录该用户名，和token
             * 其他同时在线的token会失效
             * 这里用缓存的目的是不想使用数据库查询
             * 如果没有使用踢出功能，那么凭证验证是不需要数据库参与的
             */
            if (tokenCache.containsKey(username) && !tokenCache.get(username).equals(jwt)) {
                response.setCharacterEncoding("UTF-8");
                response.setContentType("application/json");
                response.getWriter().print(JSON.toJSONString(R.error(FORCE_OFFLINE).data("被踢出，如果存在疑问，请排查用户名，密码是否泄漏")));
                return;
            }
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        chain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request){
        String bearerToken = request.getHeader(JWTConfigurer.AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7, bearerToken.length());
        }
        String jwt = request.getParameter(JWTConfigurer.AUTHORIZATION_TOKEN);
        if (StringUtils.hasText(jwt)) {
            return jwt;
        }
        return null;
    }
}
