package com.luolei.template.error;

/**
 * 权限异常
 * @author luolei
 * @createTime 2018-04-06 10:06
 */
public class AuthorizationException extends BaseException {

    public AuthorizationException() {
        super();
    }

    public AuthorizationException(String message) {
        super(message);
    }

    public AuthorizationException(String message, Throwable cause) {
        super(message, cause);
    }

    public AuthorizationException(Throwable cause) {
        super(cause);
    }
}
