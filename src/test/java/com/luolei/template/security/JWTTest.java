package com.luolei.template.security;

import com.luolei.template.config.ApplicationProperties;
import com.luolei.template.security.jwt.TokenProvider;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;
import java.util.Map;

/**
 * @author 罗雷
 * @date 2018/4/3 0003
 * @time 14:53
 */
@Slf4j
public class JWTTest {

    private String secretKey = "hello";

    @Before
    public void setUp() {
        log.info("========== 开始测试 ==========");
    }

    @After
    public void clear() {
        log.info("========== 结束测试 ==========");
    }

    @Test
    public void testGenToken() {
        Date nowDate = new Date();
        Date expireDate = new Date(nowDate.getTime() + 1000 * 2);// 2s 过期
        String token = Jwts.builder()
                .claim("first", "first")
                .claim("second", 2)
                .claim("third", "1000")
                .setSubject("luolei")
                .setIssuedAt(nowDate)
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
        log.info("token:{}", token);
    }

    @Test
    public void testGenLongToken() {
        Date nowDate = new Date();
        Date expireDate = new Date(nowDate.getTime() + 1000 * 2);// 2s 过期
        String token = Jwts.builder()
                .claim("first", "first")
                .claim("second", 2)
                .claim("third", "1000")
                .claim("longValue", "eyJhbGciOiJIUzUxMiJ9.eyJmaXJzdCI6ImZpcnN0Iiwic2Vjb25kIjoyLCJ0aGlyZCI6IjEwMDAiLCJzdWIiOiJsdW9sZWkiLCJpYXQiOjE1MjI3Mzg5MTksImV4cCI6MTUyMjczODkyMX0.BT1Bgfh4iggCihV6PMf6HqUf11eUAzpZ9sNxH065Szey7pKV4NmTO4D_NK1wefdmcCiqTcc5QfnZBVcuU3JuUA")
                .setSubject("luolei")
                .setIssuedAt(nowDate)
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
        log.info("token.length:{}", token.length());
        log.info("token:{}", token);
    }

    @Test
    public void testParseToken() {
        String token = "eyJhbGciOiJIUzUxMiJ9.eyJmaXJzdCI6ImZpcnN0Iiwic2Vjb25kIjoyLCJ0aGlyZCI6IjEwMDAiLCJzdWIiOiJsdW9sZWkiLCJpYXQiOjE1MjI3Mzg5MTksImV4cCI6MTUyMjczODkyMX0.BT1Bgfh4iggCihV6PMf6HqUf11eUAzpZ9sNxH065Szey7pKV4NmTO4D_NK1wefdmcCiqTcc5QfnZBVcuU3JuUA";
        ApplicationProperties properties = new ApplicationProperties();
        TokenProvider provider = new TokenProvider(properties);
        provider.init();
        Map<String, String> map = provider.parseToken(token);
        map.keySet().forEach(key -> {
            log.info("key:{}, value:{}", key, map.get(key));
        });
    }
}
