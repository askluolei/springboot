package com.luolei.template.domain.support;

/**
 * 认证方式
 *
 * @author 罗雷
 * @date 2018/4/2 0002
 * @time 14:21
 */
public enum AuthType {

    /**
     * 用户名密码
     */
    USER_PASS,

    /**
     * rememberMe
     */
    REFRESH_TOKEN,

    /**
     * 手机
     */
    PHONE,

    /**
     * 第三方认证
     */
    THIRD_PART
    ;
}
