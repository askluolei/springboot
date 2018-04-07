package com.luolei.template.service.mapper;

import com.google.common.collect.Sets;
import com.luolei.template.domain.Authority;
import com.luolei.template.domain.support.AuthorityType;
import com.luolei.template.domain.Role;
import com.luolei.template.domain.User;
import com.luolei.template.web.dto.UserDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Java6Assertions.assertThat;

/**
 * @author 罗雷
 * @date 2018/4/3 0003
 * @time 11:16
 */
@Slf4j
//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = Application.class)
public class UserMapperTest {

    private UserMapper userMapper;

    private User user;

    @Before
    public void setUp() {
        userMapper = UserMapper.INSTANCE;
        Set<Authority> authorities = new HashSet<>();
        Authority authority = new Authority();
        authority.setId(1L);
        authority.setAuthority("user:add");
        authority.setAuthorityCn("添加用户");
        authority.setType(AuthorityType.ENTITY);
        authorities.add(authority);

        authority = new Authority();
        authority.setId(2L);
        authority.setAuthority("user:delete");
        authority.setAuthorityCn("删除用户");
        authority.setType(AuthorityType.ENTITY);
        authorities.add(authority);

        Role role = new Role();
        role.setName("admin");
        role.setNameCn("管理员");
        role.setAuthorities(authorities);

        user = new User();
        user.setUsername("admin");
        user.setPassword("admin");
        user.setFirstName("admin");
        user.setLastName("admin");
        user.setEmail("admin@localhost.com");
        user.setActivated(true);
        user.setLangKey("zh");
        user.setActivationKey("activationKey admin");
        user.setResetKey("resetKey admin");
        user.setRoles(Sets.newHashSet(role));
        log.info("========== 开始测试 ==========");
    }

    @After
    public void clear() {
        log.info("========== 结束测试 ==========");
    }

    @Test
    public void test() {
        UserDto userDto = userMapper.fromUser(user);
        log.info(userDto.toString());
        assertThat(userDto.getUsername()).isEqualTo("admin");
    }
}
