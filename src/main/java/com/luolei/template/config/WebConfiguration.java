package com.luolei.template.config;

import com.luolei.template.support.resolver.RequestUriResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * web mvc 的配置
 *
 * @author luolei
 * @createTime 2018-03-31 10:46
 */
@Configuration
public class WebConfiguration implements WebMvcConfigurer {

    /**
     * 添加请求参数处理类
     * @param resolvers
     */
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new RequestUriResolver());
    }
}
