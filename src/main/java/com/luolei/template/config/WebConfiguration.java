package com.luolei.template.config;

import com.luolei.template.support.resolver.RequestIPResolver;
import com.luolei.template.support.resolver.RequestPlatformResolver;
import com.luolei.template.support.resolver.RequestUriResolver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * web mvc 的配置
 *
 * @author luolei
 * @createTime 2018-03-31 10:46
 */
@Slf4j
@Configuration
public class WebConfiguration implements WebMvcConfigurer {

    private final ApplicationProperties  applicationProperties;

    public WebConfiguration(ApplicationProperties applicationProperties) {
        this.applicationProperties =  applicationProperties;
    }

    /**
     * 添加请求参数处理类
     * @param resolvers
     */
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new RequestUriResolver());
        resolvers.add(new RequestPlatformResolver());
        resolvers.add(new RequestIPResolver());
    }

    /**
     * cors 跨域配置
     * @return
     */
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = applicationProperties.getCors();
        if (config.getAllowedOrigins() != null && !config.getAllowedOrigins().isEmpty()) {
            log.debug("Registering CORS filter");
            source.registerCorsConfiguration("/api/**", config);
            source.registerCorsConfiguration("/v2/api-docs", config);
        }
        return new CorsFilter(source);
    }
}
