package com.luolei.template.repository;

import com.google.common.collect.Sets;
import com.luolei.template.Application;
import com.luolei.template.domain.Authority;
import com.luolei.template.domain.support.AuthorityType;
import com.luolei.template.domain.Role;
import com.luolei.template.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author luolei
 * @createTime 2018-04-02 22:54
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@Transactional
public class UserRepositoryTest {

    @Autowired
    private AuthorityRepository authorityRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

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

        Role role1 = new Role();
        role1.setName("admin");
        role1.setNameCn("管理员");
        List<Authority> all = authorityRepository.findAll();
        role1.setAuthorities(all.stream().collect(Collectors.toSet()));
        roleRepository.save(role1);

        Role role2 = new Role();
        role2.setName("user");
        role2.setNameCn("普通用户");
        List<Authority> byType = authorityRepository.findAllByType(AuthorityType.URL);
        role2.setAuthorities(byType.stream().collect(Collectors.toSet()));
        roleRepository.save(role2);

        User user = new User();
        user.setUsername("admin");
        user.setPassword("admin");
        user.setFirstName("admin");
        user.setLastName("admin");
        user.setEmail("admin@localhost.com");
        user.setActivated(true);
        user.setLangKey("zh");
        user.setActivationKey("activationKey admin");
        user.setResetKey("resetKey admin");
        user.setRoles(Sets.newHashSet(role1));
        userRepository.save(user);

        user = new User();
        user.setUsername("user");
        user.setPassword("user");
        user.setFirstName("user");
        user.setLastName("user");
        user.setEmail("user@localhost.com");
        user.setActivated(true);
        user.setLangKey("zh");
        user.setActivationKey("activationKey user");
        user.setResetKey("resetKey user");
        user.setRoles(Sets.newHashSet(role2));
        userRepository.save(user);

        user = new User();
        user.setUsername("unactivate");
        user.setPassword("unactivate");
        user.setFirstName("unactivate");
        user.setLastName("unactivate");
        user.setEmail("unactivate@localhost.com");
        user.setActivated(false);
        user.setLangKey("zh");
        user.setActivationKey("activate unactivate");
        user.setResetKey("resetKey unactivate");
        user.setRoles(Sets.newHashSet(role2));
        userRepository.save(user);
        log.info("========== 开始测试 ==========");
    }

    @After
    public void clear() {
        log.info("========== 结束测试 ==========");
    }

    @Test
    public void testFindAll() {
        List<User> all = userRepository.findAll();
        assertThat(all.size()).isEqualTo(3);
    }

    @Test
    public void testFindOneByUsername() {
        Optional<User> optional = userRepository.findOneByUsername("admin");
        assertThat(optional.isPresent()).isTrue();
        optional = userRepository.findOneByUsername("unknown");
        assertThat(optional.isPresent()).isFalse();
    }

    @Test
    public void testFindOneWithRolesByUsername() {
        Optional<User> optional = userRepository.findOneWithRolesByUsername("admin");
        assertThat(optional.isPresent()).isTrue();
        User user = optional.get();
        assertThat(user.getUsername()).isEqualTo("admin");
        Set<Role> roles = user.getRoles();
        assertThat(roles.size()).isEqualTo(1);
        Role role = roles.stream().findFirst().get();
        //需要注意的是，只是调用size 是不会触发延时加载的
        log.info("try to get authorities");
        Set<Authority> authorities = role.getAuthorities();
        assertThat(authorities.size()).isEqualTo(5);
        // 这里触发延时加载,貌似在日志里面也没看到延时加载打印的sql日志
        authorities.stream().forEach(authority -> log.info(authority.toString()));
    }

    @Test
    public void testFindOneByEmailIgnoreCase() {
        Optional<User> optional = userRepository.findOneByEmailIgnoreCase("admin@localhost.com");
        assertThat(optional.isPresent()).isTrue();
        userRepository.findOneByEmailIgnoreCase("ADMIN@localhost.com");
        assertThat(optional.isPresent()).isTrue();
    }

    @Test
    public void testFindOneWithRolesByEmailIgnoreCase() {
        Optional<User> optional = userRepository.findOneWithRolesByEmailIgnoreCase("ADMIN@localhost.com");
        assertThat(optional.isPresent()).isTrue();
        User user = optional.get();
        assertThat(user.getUsername()).isEqualTo("admin");
        assertThat(user.getEmail()).isEqualTo("admin@localhost.com");
        Set<Role> roles = user.getRoles();
        assertThat(roles.size()).isEqualTo(1);
        Role role = roles.stream().findFirst().get();
        //需要注意的是，只是调用size 是不会触发延时加载的
        log.info("try to get authorities");
        Set<Authority> authorities = role.getAuthorities();
        assertThat(authorities.size()).isEqualTo(5);
        // 这里触发延时加载,貌似在日志里面也没看到延时加载打印的sql日志
        authorities.stream().forEach(authority -> log.info(authority.toString()));
    }

    @Test
    public void testFindOneByActivationKey() {
        Optional<User> optional = userRepository.findOneByActivationKey("activationKey admin");
        assertThat(optional.isPresent()).isTrue();
        optional = userRepository.findOneByActivationKey("activationKey admin123");
        assertThat(optional.isPresent()).isFalse();
    }

    @Test
    public void testFindAllByActivatedIsFalseAndCreatedDateBefore() {
        List<User> users = userRepository.findAllByActivatedIsFalseAndCreatedDateBefore(Instant.now().plusSeconds(60));
        assertThat(users.size()).isEqualTo(1);
        assertThat(users.get(0).getUsername()).isEqualTo("unactivate");
    }

    @Test
    public void testFindOneByResetKey() {
        Optional<User> optional = userRepository.findOneByResetKey("resetKey admin");
        assertThat(optional.isPresent()).isTrue();
        optional = userRepository.findOneByResetKey("resetKey admin123");
        assertThat(optional.isPresent()).isFalse();
    }

    @Test
    public void testFindOneWithRolesById() {
        List<User> all = userRepository.findAll();
        assertThat(all.size()).isEqualTo(3);
        long id = all.get(0).getId();
        Optional<User> optional = userRepository.findOneWithRolesById(id);
        assertThat(optional.isPresent()).isTrue();
    }

    @Test
    public void testFindAllByUsernameNot() {
        Page<User> page = userRepository.findAllByUsernameNot(PageRequest.of(0, 10), "unactivate");
        assertThat(page.getContent().size()).isEqualTo(2);
    }
}
