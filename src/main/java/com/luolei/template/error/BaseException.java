package com.luolei.template.error;

/**
 * 应用里面的基础异常
 *
 * @author luolei
 * @createTime 2018-03-24 12:13
 */
public class BaseException extends RuntimeException {

    public BaseException() {
        super();
    }

    public BaseException(String message) {
        super(message);
    }

    public BaseException(String message, Throwable cause) {
        super(message, cause);
    }

    public BaseException(Throwable cause) {
        super(cause);
    }
}
