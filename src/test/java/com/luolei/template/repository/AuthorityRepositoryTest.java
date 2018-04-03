package com.luolei.template.repository;

import com.luolei.template.Application;
import com.luolei.template.domain.Authority;
import com.luolei.template.domain.support.AuthorityType;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author 罗雷
 * @date 2018/4/3 0003
 * @time 8:59
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@Transactional
public class AuthorityRepositoryTest {

    @Autowired
    private AuthorityRepository authorityRepository;

    /**
     * 初始化5个权限
     */
    @Before
    public void setUp() {
        Authority authority = new Authority();
        authority.setAuthority("user:add");
        authority.setAuthorityCn("添加用户");
        authority.setType(AuthorityType.ENTITY);
        authorityRepository.save(authority);

        authority = new Authority();
        authority.setAuthority("user:delete");
        authority.setAuthorityCn("删除用户");
        authority.setType(AuthorityType.ENTITY);
        authorityRepository.save(authority);

        authority = new Authority();
        authority.setAuthority("user:update");
        authority.setAuthorityCn("修改用户");
        authority.setType(AuthorityType.ENTITY);
        authorityRepository.save(authority);

        authority = new Authority();
        authority.setAuthority("user:query");
        authority.setAuthorityCn("查询用户");
        authority.setType(AuthorityType.ENTITY);
        authorityRepository.save(authority);

        authority = new Authority();
        authority.setAuthority("/api/user");
        authority.setAuthorityCn("查询用户API");
        authority.setType(AuthorityType.URL);
        authorityRepository.save(authority);
        log.info("========== 开始测试 ==========");
    }

    @After
    public void clear() {
        log.info("========== 结束测试 ==========");
    }

    @Test
    public void testFindAll() {
        List<Authority> all = authorityRepository.findAll();
        assertThat(all.size()).isEqualTo(5);
    }

    @Test
    public void testFindByType() {
        List<Authority> byEntity = authorityRepository.findAllByType(AuthorityType.ENTITY);
        assertThat(byEntity.size()).isEqualTo(4);
        List<Authority> byUrl = authorityRepository.findAllByType(AuthorityType.URL);
        assertThat(byUrl.size()).isEqualTo(1);
    }

    @Test
    public void testFindByAuthority() {
        Optional<Authority> optional = authorityRepository.findOneByAuthority("user:add");
        assertThat(optional.isPresent()).isTrue();
        optional = authorityRepository.findOneByAuthority("user:unknown");
        assertThat(optional.isPresent()).isFalse();
    }
}
