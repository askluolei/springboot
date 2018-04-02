package com.luolei.template.config;

import com.luolei.template.aop.LoggingAspect;
import com.luolei.template.support.Constants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;

/**
 * AOP 日志配置, 开发模式下开启
 * 这里开启 aop 支持
 *
 * @author luolei
 * @createTime 2018-03-24 12:43
 */
@Configuration
@EnableAspectJAutoProxy
public class LoggingAspectConfiguration {

    @Bean
    @Profile(Constants.SPRING_PROFILE_DEVELOPMENT)
    public LoggingAspect loggingAspect(Environment env) {
        return new LoggingAspect(env);
    }
}
