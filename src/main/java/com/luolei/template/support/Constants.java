package com.luolei.template.support;

/**
 * 常量
 *
 * @author luolei
 * @createTime 2018-03-24 12:14
 */
public interface Constants {

    String SPRING_PROFILE_DEVELOPMENT = "dev";
    String SPRING_PROFILE_PRODUCTION = "prod";
    String SPRING_PROFILE_TEST = "test";
    String SPRING_PROFILE_SWAGGER = "swagger";

    String LOGIN_REGEX = "^[_'.@A-Za-z0-9-]*$";
    String SYSTEM_ACCOUNT = "system";
    String ANONYMOUS_USER = "anonymoususer";
    String DEFAULT_LANGUAGE = "zh-cn";

    int PASSWORD_MIN_LENGTH = 4;
    int PASSWORD_MAX_LENGTH = 16;

    String ROLE_PREFIX = "ROLE_";
    String ROLE_ADMIN = "admin";
}
