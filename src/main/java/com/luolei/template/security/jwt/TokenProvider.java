package com.luolei.template.security.jwt;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.luolei.template.config.ApplicationProperties;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author 罗雷
 * @date 2018/4/2 0002
 * @time 15:03
 */
@Slf4j
@Component
public class TokenProvider {

    public static final String AUTHORITIES_KEY = "auth";
    public static final String RANDOM_KEY = "random";
    public static final String TOKEN_TYPE_KEY = "type";
    public static final String TOKEN_TYPE = "accessToken";
    public static final String REFRESH_TOKEN_TYPE = "refreshToken";
    public static final String TEMP_TOKEN_TYPE = "tempToken";
    public static final String USER_ID_KEY = "userId";

    // jwt 里面默认的key
    public static final String SUBJECT_KEY = "sub";
    public static final String ISSUED_AT_KEY = "iat";
    public static final String EXPIRED_DATE_KEY = "exp";

    private String secretKey;
    private long tokenValidityInMilliseconds;
    private long tokenValidityInMillisecondsForRememberMe;

    private final ApplicationProperties applicationProperties;

    public TokenProvider(ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
    }

    @PostConstruct
    public void init() {
        this.secretKey = applicationProperties.getSecurity().getAuthentication().getJwt().getSecret();
        this.tokenValidityInMilliseconds = 1000 * applicationProperties.getSecurity().getAuthentication().getJwt().getTokenValidityInSeconds();
        this.tokenValidityInMillisecondsForRememberMe = 1000 * applicationProperties.getSecurity().getAuthentication().getJwt().getTokenValidityInSecondsForRememberMe();
    }

    public long getTokenValidityInMilliseconds() {
        return tokenValidityInMilliseconds;
    }

    public long getTokenValidityInMillisecondsForRememberMe() {
        return tokenValidityInMillisecondsForRememberMe;
    }

    private static final String DOT = ",";

    private String generateAuthoritiesString(Authentication authentication) {
        StringBuilder stringBuilder = new StringBuilder();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        for (GrantedAuthority grantedAuthority : authorities) {
            stringBuilder.append(grantedAuthority.getAuthority()).append(DOT);
        }
        if (stringBuilder.length() > 0) {
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        }
        return stringBuilder.toString();
    }

    /**
     * 生成凭证的时候将权限信息都写进去了，会导致token变的非常大，但是后面校验的时候不需要从数据库查询权限信息了
     * 为了限制token的长度，授权的时候尽量使用通配符
     * @param authentication
     * @return
     */
    public String createAccessToken(Authentication authentication) {
        Date nowDate = new Date();
        //过期时间
        Date expireDate = new Date(nowDate.getTime() + tokenValidityInMilliseconds);
        return Jwts.builder()
                .claim(TOKEN_TYPE_KEY, TOKEN_TYPE)
                .claim(AUTHORITIES_KEY, generateAuthoritiesString(authentication))
                .setSubject(authentication.getName())
                .setIssuedAt(nowDate)
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }

    /**
     * 生成凭证，带随机数
     * @param authentication
     * @param random
     * @return
     */
    public String createAccessToken(Authentication authentication, long random) {
        Date nowDate = new Date();
        //过期时间
        Date expireDate = new Date(nowDate.getTime() + tokenValidityInMilliseconds);
        return Jwts.builder()
                .claim(TOKEN_TYPE_KEY, TOKEN_TYPE)
                .claim(RANDOM_KEY, random)
                .claim(AUTHORITIES_KEY, generateAuthoritiesString(authentication))
                .setSubject(authentication.getName())
                .setIssuedAt(nowDate)
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }

    /**
     * 生成刷新凭证
     * @param authentication
     * @return
     */
    public String createRefreshToken(Authentication authentication) {
        Date nowDate = new Date();
        //过期时间 7天
        Date expireDate = new Date(nowDate.getTime() + tokenValidityInMillisecondsForRememberMe);
        return Jwts.builder()
                .claim(TOKEN_TYPE_KEY, REFRESH_TOKEN_TYPE)
                .claim(AUTHORITIES_KEY, generateAuthoritiesString(authentication))
                .setSubject(authentication.getName())
                .setIssuedAt(nowDate)
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }

    /**
     * 生成刷新凭证，带随机数
     * @param authentication
     * @param random
     * @return
     */
    public String createRefreshToken(Authentication authentication, long random) {
        Date nowDate = new Date();
        //过期时间 7天
        Date expireDate = new Date(nowDate.getTime() + tokenValidityInMillisecondsForRememberMe);
        return Jwts.builder()
                .claim(TOKEN_TYPE_KEY, REFRESH_TOKEN_TYPE)
                .claim(RANDOM_KEY, random)
                .claim(AUTHORITIES_KEY, generateAuthoritiesString(authentication))
                .setSubject(authentication.getName())
                .setIssuedAt(nowDate)
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }

    /**
     * 生成一个给定有效期的临时token
     * @param expiredSecond
     * @return
     */
    public String createTempToken(int expiredSecond) {
        Date nowDate = new Date();
        Date expireDate = new Date(nowDate.getTime() + expiredSecond * 1000);
        return Jwts.builder()
                .claim(TOKEN_TYPE_KEY, TEMP_TOKEN_TYPE)
                .setSubject("system")
                .setIssuedAt(nowDate)
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }

    /**
     * 生成一个给定有效期的临时token，附带随机数
     * @param expiredSecond
     * @param random
     * @return
     */
    public String createTempToken(int expiredSecond, long random) {
        Date nowDate = new Date();
        Date expireDate = new Date(nowDate.getTime() + expiredSecond * 1000);
        return Jwts.builder()
                .claim(TOKEN_TYPE_KEY, TEMP_TOKEN_TYPE)
                .claim(RANDOM_KEY, random)
                .setSubject("system")
                .setIssuedAt(nowDate)
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }

    /**
     * 验证 token 是否有效
     * @param token
     * @return
     */
    public boolean validateToken(String token, String tokenType) {
        try {
            Claims claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
            /**
             * token 还
             */
            if (!claims.containsKey(TOKEN_TYPE_KEY) || !claims.get(TOKEN_TYPE_KEY).equals(tokenType)) {
                return false;
            }
            return true;
        } catch (SignatureException e) {
            log.info("Invalid JWT signature.");
            log.trace("Invalid JWT signature trace: {}", e);
        } catch (MalformedJwtException e) {
            log.info("Invalid JWT token.");
            log.trace("Invalid JWT token trace: {}", e);
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT token.");
            log.trace("Expired JWT token trace: {}", e);
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT token.");
            log.trace("Unsupported JWT token trace: {}", e);
        } catch (IllegalArgumentException e) {
            log.info("JWT token compact of handler are invalid.");
            log.trace("JWT token compact of handler are invalid trace: {}", e);
        }
        return false;
    }

    /**
     *  通过token 获取 spring-security 的凭证
     * @param token
     * @return
     */
    public Authentication getAuthentication(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
        Collection<? extends GrantedAuthority> authorities = Collections.EMPTY_LIST;
        if (claims.containsKey(AUTHORITIES_KEY)) {
            authorities =
                    Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                            .map(SimpleGrantedAuthority::new)
                            .collect(Collectors.toList());
        }
        org.springframework.security.core.userdetails.User principal = new org.springframework.security.core.userdetails.User(claims.getSubject(), "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    /**
     * 不对token 做校验，直接解析数据
     * @param token
     * @return
     */
    public Map<String, String> parseToken(String token) {
        String[] words = StrUtil.split(token, ".");
        String header = words[0];
        String body = words[1];
        JSONObject headerJson = JSON.parseObject(new String(Base64.getDecoder().decode(header)));
        JSONObject bodyJson = JSON.parseObject(new String(Base64.getDecoder().decode(body)));
        Map<String, String> result = new HashMap<>();
        for (String key : headerJson.keySet()) {
            result.put(key, String.valueOf(headerJson.get(key)));
        }
        for (String key : bodyJson.keySet()) {
            result.put(key, String.valueOf(bodyJson.get(key)));
        }
        return result;
    }

    /**
     * 从token 里面获取随机数,不校验
     * @param token
     * @return
     */
    public long getRamdom(String token) {
        return Long.parseLong(parseToken(token).get(RANDOM_KEY));
    }

    /**
     * 从token中获取用户名,不校验
     * @param token
     * @return
     */
    public String getUsername(String token) {
        return parseToken(token).get(SUBJECT_KEY);
    }

    /**
     * 获取token信息
     * @param token
     * @return
     */
    public Claims getClaimByToken(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(secretKey)
                    .require(TOKEN_TYPE_KEY, TOKEN_TYPE)
                    .parseClaimsJws(token)
                    .getBody();
        }catch (Exception e){
            return null;
        }
    }
}
