package com.luolei.template.repository;

import com.luolei.template.Application;
import com.luolei.template.domain.Authority;
import com.luolei.template.domain.support.AuthorityType;
import com.luolei.template.domain.Role;
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
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author 罗雷
 * @date 2018/4/3 0003
 * @time 9:33
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@Transactional
public class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private AuthorityRepository authorityRepository;

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

        Role role = new Role();
        role.setName("admin");
        role.setNameCn("管理员");
        List<Authority> all = authorityRepository.findAll();
        role.setAuthorities(all.stream().collect(Collectors.toSet()));
        roleRepository.save(role);

        role = new Role();
        role.setName("user");
        role.setNameCn("普通用户");
        List<Authority> byType = authorityRepository.findAllByType(AuthorityType.URL);
        role.setAuthorities(byType.stream().collect(Collectors.toSet()));
        roleRepository.save(role);
        log.info("========== 开始测试 ==========");
    }

    @After
    public void clear() {
        log.info("========== 结束测试 ==========");
    }

    @Test
    public void testFindAll() {
        List<Role> all = roleRepository.findAll();
        assertThat(all.size()).isEqualTo(2);
    }

    /**
     *
     SELECT
     role0_.id AS id1_2_,
     role0_.created_by AS created_2_2_,
     role0_.created_date AS created_3_2_,
     role0_.last_modified_by AS last_mod4_2_,
     role0_.last_modified_date AS last_mod5_2_,
     role0_.c_explanation AS c_explan6_2_,
     role0_. NAME AS name7_2_,
     role0_.name_cn AS name_cn8_2_
     FROM
     t_role role0_
     WHERE
     role0_. NAME =?
     */
    @Test
    public void testFindOneByName() {
        Optional<Role> admin = roleRepository.findOneByName("admin");
        assertThat(admin.isPresent()).isTrue();
    }

    /**
     *
     SELECT
     role0_.id AS id1_2_0_,
     authority2_.id AS id1_0_1_,
     role0_.created_by AS created_2_2_0_,
     role0_.created_date AS created_3_2_0_,
     role0_.last_modified_by AS last_mod4_2_0_,
     role0_.last_modified_date AS last_mod5_2_0_,
     role0_.c_explanation AS c_explan6_2_0_,
     role0_. NAME AS name7_2_0_,
     role0_.name_cn AS name_cn8_2_0_,
     authority2_.created_by AS created_2_0_1_,
     authority2_.created_date AS created_3_0_1_,
     authority2_.last_modified_by AS last_mod4_0_1_,
     authority2_.last_modified_date AS last_mod5_0_1_,
     authority2_.authority AS authorit6_0_1_,
     authority2_.authority_cn AS authorit7_0_1_,
     authority2_.c_explanation AS c_explan8_0_1_,
     authority2_.type AS type9_0_1_,
     authoritie1_.role_id AS role_id1_3_0__,
     authoritie1_.authority_id AS authorit2_3_0__
     FROM
     t_role role0_
     LEFT OUTER JOIN t_role_authority authoritie1_ ON role0_.id = authoritie1_.role_id
     LEFT OUTER JOIN jhi_authority authority2_ ON authoritie1_.authority_id = authority2_.id
     WHERE
     role0_. NAME =?
     */
    @Test
    public void testFindOneWithAuthoritiesByName() {
        Optional<Role> admin = roleRepository.findOneWithAuthoritiesByName("admin");
        assertThat(admin.isPresent()).isTrue();
        Role role = admin.get();
        assertThat(role.getName()).isEqualTo("admin");
        Set<Authority> authorities = role.getAuthorities();
        assertThat(authorities.size()).isEqualTo(5);
    }
}
