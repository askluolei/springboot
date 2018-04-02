package com.luolei.template.repository;

import com.luolei.template.Application;
import com.luolei.template.domain.Authority;
import com.luolei.template.domain.AuthorityType;
import com.luolei.template.domain.Role;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author luolei
 * @createTime 2018-04-02 22:54
 */
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
        List<Authority> authorities = new ArrayList<>();
        Authority authority1 = new Authority();
        authority1.setAuthority("user:add");
        authority1.setAuthorityCn("添加用户");
        authority1.setType(AuthorityType.ENTITY);
        authorities.add(authority1);
        Authority authority2 = new Authority();
        authority2.setAuthority("user:delete");
        authority2.setAuthorityCn("添加用户");
        authority2.setType(AuthorityType.ENTITY);
        authorities.add(authority2);
        Authority authority3 = new Authority();
        authority3.setAuthority("user:update");
        authority3.setAuthorityCn("添加用户");
        authority3.setType(AuthorityType.ENTITY);
        authorities.add(authority3);
        Authority authority4 = new Authority();
        authority4.setAuthority("user:query");
        authority4.setAuthorityCn("添加用户");
        authority4.setType(AuthorityType.ENTITY);
        authorities.add(authority4);
        authorities = authorityRepository.saveAll(authorities);

        Role role1 = new Role();

    }
}
