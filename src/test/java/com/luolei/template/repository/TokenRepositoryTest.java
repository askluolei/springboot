package com.luolei.template.repository;

import com.luolei.template.Application;
import com.luolei.template.domain.Token;
import com.luolei.template.domain.support.AuthType;
import com.luolei.template.domain.support.RequestPlatform;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author luolei
 * @createTime 2018-04-06 17:30
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@Transactional
public class TokenRepositoryTest {

    @Autowired
    private TokenRepository tokenRepository;

    @Before
    public void setUp() {
        Token token = new Token();
        token.setUsername("user");
        token.setAccessToken("accessToken_user1");
        token.setRefreshToken("refreshToken_user1");
        token.setRandom(1L);
        token.setPlatform(RequestPlatform.PC);
        token.setLoginIp("192.168.1.1");
        token.setAuthType(AuthType.USER_PASS);
        token.setExpireTime(Instant.now().plusSeconds(10 * 60L));
        tokenRepository.save(token);

        token = new Token();
        token.setUsername("user");
        token.setAccessToken("accessToken_user2");
        token.setRefreshToken("refreshToken_user2");
        token.setRandom(2L);
        token.setPlatform(RequestPlatform.PC);
        token.setLoginIp("192.168.1.1");
        token.setAuthType(AuthType.USER_PASS);
        token.setExpireTime(Instant.now().plusSeconds(20 * 60L));
        tokenRepository.save(token);

        token = new Token();
        token.setUsername("user");
        token.setAccessToken("accessToken_user3");
        token.setRefreshToken("refreshToken_user3");
        token.setRandom(3L);
        token.setPlatform(RequestPlatform.ANDROID);
        token.setLoginIp("192.168.1.1");
        token.setAuthType(AuthType.PHONE);
        token.setExpireTime(Instant.now().plusSeconds(10 * 60L));
        tokenRepository.save(token);

        token = new Token();
        token.setUsername("user");
        token.setAccessToken("accessToken_user4");
        token.setRefreshToken("refreshToken_user4");
        token.setRandom(4L);
        token.setPlatform(RequestPlatform.PC);
        token.setLoginIp("192.168.1.2");
        token.setAuthType(AuthType.USER_PASS);
        token.setExpireTime(Instant.now().plusSeconds(10 * 60L));
        tokenRepository.save(token);
        log.info("========== 开始测试 ==========");
    }

    @After
    public void clear() {
        log.info("========== 结束测试 ==========");
    }

    @Test
    public void findOneByAccessToken() {
        Optional<Token> optional = tokenRepository.findOneByAccessToken("accessToken_user1");
        assertThat(optional.isPresent()).isTrue();
        optional = tokenRepository.findOneByAccessToken("accessToken_user1111");
        assertThat(optional.isPresent()).isFalse();
    }

    @Test
    public void findOneByRefreshToken() {
        Optional<Token> optional = tokenRepository.findOneByRefreshToken("refreshToken_user1");
        assertThat(optional.isPresent()).isTrue();
        optional = tokenRepository.findOneByRefreshToken("refreshToken_user1111");
        assertThat(optional.isPresent()).isFalse();
    }

    @Test
    public void findFirstByUsernameAndLoginIpAndPlatformAndAuthTypeAndExpireTimeAfterOrderByExpireTimeDesc() {
        Optional<Token> optional = tokenRepository.findFirstByUsernameAndLoginIpAndPlatformAndAuthTypeAndExpireTimeAfterOrderByExpireTimeDesc("user", "192.168.1.1", RequestPlatform.PC, AuthType.USER_PASS, Instant.now());
        assertThat(optional.isPresent()).isTrue();
        Token token = optional.get();
        assertThat(token.getAccessToken()).isEqualTo("accessToken_user2");
        optional = tokenRepository.findFirstByUsernameAndLoginIpAndPlatformAndAuthTypeAndExpireTimeAfterOrderByExpireTimeDesc("user12", "192.168.1.1", RequestPlatform.PC, AuthType.USER_PASS, Instant.now());
        assertThat(optional.isPresent()).isFalse();
    }
}
