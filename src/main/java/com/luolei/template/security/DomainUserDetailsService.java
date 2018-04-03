package com.luolei.template.security;

import com.luolei.template.domain.Authority;
import com.luolei.template.domain.Role;
import com.luolei.template.domain.User;
import com.luolei.template.repository.UserRepository;
import com.luolei.template.support.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @author 罗雷
 * @date 2018/4/2 0002
 * @time 16:48
 */
@Slf4j
@Component("userDetailsService")
public class DomainUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public DomainUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        log.debug("Authenticating {}", username);
        String lowercaseUsername = username.toLowerCase(Locale.ENGLISH);
        Optional<User> optional = userRepository.findOneWithRolesByUsername(lowercaseUsername);
        optional.map(user -> {
            if (!user.isActivated()) {
                throw new UserNotActivatedException("User " + lowercaseUsername + " was not activated");
            }
            // 权限表达式
            List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
            Set<Role> roles = user.getRoles();
            for (Role role: roles) {
                // 添加角色
                grantedAuthorities.add(new SimpleGrantedAuthority(Constants.ROLE_PREFIX + role.getName()));
                if (Objects.nonNull(role.getAuthorities())) {
                    for (Authority authority : role.getAuthorities()) {
                        grantedAuthorities.add(new SimpleGrantedAuthority(authority.getAuthority()));
                    }
                }
            }
            return new org.springframework.security.core.userdetails.User(lowercaseUsername, user.getPassword(), grantedAuthorities);
        }).orElseThrow(() -> new UsernameNotFoundException("User " + lowercaseUsername + " was not found in the " + "database"));
        return null;
    }
}
