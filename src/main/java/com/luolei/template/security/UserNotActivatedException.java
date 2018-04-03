package com.luolei.template.security;

import org.springframework.security.core.AuthenticationException;

/**
 * 用户未激活异常
 *
 * @author 罗雷
 * @date 2018/1/3 0003
 * @time 15:34
 */
public class UserNotActivatedException extends AuthenticationException {

    private static final long serialVersionUID = 1L;

    public UserNotActivatedException(String message) {
        super(message);
    }

    public UserNotActivatedException(String message, Throwable t) {
        super(message, t);
    }
}
