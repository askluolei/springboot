package com.luolei.template.config;

import com.google.common.collect.Sets;
import com.luolei.template.domain.Authority;
import com.luolei.template.domain.Role;
import com.luolei.template.domain.User;
import com.luolei.template.domain.support.AuthorityType;
import com.luolei.template.repository.AuthorityRepository;
import com.luolei.template.repository.RoleRepository;
import com.luolei.template.repository.UserRepository;
import com.luolei.template.support.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 开发模式才使用的配置
 * 就是添加一些用户之类的
 *
 * @author luolei
 * @createTime 2018-04-06 10:31
 */
@Slf4j
@Profile({Constants.SPRING_PROFILE_DEVELOPMENT})
@Configuration
public class DevConfig implements CommandLineRunner{

    private final AuthorityRepository authorityRepository;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DevConfig(AuthorityRepository authorityRepository, RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.authorityRepository = authorityRepository;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * 初始化一些数据
     */
    @Override
    public void run(String... args) throws Exception {
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
}
