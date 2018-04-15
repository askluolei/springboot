package com.luolei.template.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Sets;
import com.luolei.template.domain.Authority;
import com.luolei.template.domain.Role;
import com.luolei.template.domain.User;
import com.luolei.template.domain.support.AuthorityType;
import com.luolei.template.repository.AuthorityRepository;
import com.luolei.template.repository.RoleRepository;
import com.luolei.template.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.context.WebApplicationContext;

/**
 * @author luolei
 * @createTime 2018-04-15 18:55
 */
@Slf4j
public class AbstractTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AuthorityRepository authorityRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public void before() throws Exception {}

    public void initUser(AuthorityRepository authorityRepository, RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        Authority p1 = new Authority();
        p1.setType(AuthorityType.SERVICE);
        p1.setAuthority("p1");
        p1.setAuthorityCn("权限1");
        p1 = authorityRepository.save(p1);

        Authority p2 = new Authority();
        p2.setType(AuthorityType.SERVICE);
        p2.setAuthority("p2");
        p2.setAuthorityCn("权限2");
        p2 = authorityRepository.save(p2);

        Authority p3 = new Authority();
        p3.setType(AuthorityType.SERVICE);
        p3.setAuthority("p3");
        p3.setAuthorityCn("权限3");
        p3 = authorityRepository.save(p3);

        Authority p4 = new Authority();
        p4.setType(AuthorityType.SERVICE);
        p4.setAuthority("p4");
        p4.setAuthorityCn("权限4");
        p4 = authorityRepository.save(p4);

        Authority p5 = new Authority();
        p5.setType(AuthorityType.SERVICE);
        p5.setAuthority("p5");
        p5.setAuthorityCn("权限5");
        p5 = authorityRepository.save(p5);

        Role userRole = new Role();
        userRole.setName("user");
        userRole.setNameCn("普通用户");
        userRole.setAuthorities(Sets.newHashSet(p1, p4));
        userRole = roleRepository.save(userRole);

        Role adminRole = new Role();
        adminRole.setName("admin");
        adminRole.setNameCn("管理员");
        adminRole.setAuthorities(Sets.newHashSet(p1, p2, p3, p5));
        adminRole = roleRepository.save(adminRole);

        User user = new User();
        user.setUsername("user");
        user.setPassword(passwordEncoder.encode("user"));
        user.setFirstName("user");
        user.setLastName("user");
        user.setEmail("user@localhost.com");
        user.setActivated(true);
        user.setLangKey("zh");
        user.setActivationKey("activationKey user");
        user.setResetKey("resetKey user");
        user.setRoles(Sets.newHashSet(userRole));
        user = userRepository.save(user);

        User admin = new User();
        admin.setUsername("admin");
        admin.setPassword(passwordEncoder.encode("admin"));
        admin.setFirstName("admin");
        admin.setLastName("admin");
        admin.setEmail("admin@localhost.com");
        admin.setActivated(true);
        admin.setLangKey("zh");
        admin.setActivationKey("activationKey admin");
        admin.setResetKey("resetKey admin");
        admin.setRoles(Sets.newHashSet(adminRole));
        admin = userRepository.save(admin);
    }

    @Before
    public void setUp() throws Exception {
        initUser(authorityRepository, roleRepository, userRepository, passwordEncoder);
        before();
        log.info("========== 开始测试 ==========");
    }

    public void after() throws Exception {}

    @After
    public void clear() throws Exception {
        after();
        log.info("========== 结束测试 ==========");
    }

}
