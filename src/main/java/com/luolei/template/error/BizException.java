package com.luolei.template.error;

/**
 * 应用里面的基础异常
 *
 * @author luolei
 * @createTime 2018-03-24 12:13
 */
public class BizException extends RuntimeException {

    /**
     * 错误码
     * R 类里面定义的常量响应码
     * 如果定义了，那么 R 响应的时候就取这个code，否则就是统一响应码了
     */
    private String code;

    public BizException() {
        super();
    }

    public BizException(String message) {
        super(message);
    }

    public BizException(String message, Throwable cause) {
        super(message, cause);
    }

    public BizException(Throwable cause) {
        super(cause);
    }

    public BizException withCode(String code) {
        this.code = code;
        return this;
    }

    public String getCode() {
        return this.code;
    }
}
