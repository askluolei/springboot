package com.luolei.template.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * swagger 的配置
 * 可以扫描自定义注解和 spring 的注解生成 api 文档
 *
 * @author luolei
 * @createTime 2018-03-24 17:00
 */
@Configuration
@EnableSwagger2
public class SwaggerConfiguration {

    private final ApplicationProperties.Swagger swagger;

    public SwaggerConfiguration(ApplicationProperties applicationProperties) {
        this.swagger = applicationProperties.getSwagger();
    }

    @Bean
    public Docket docket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage(swagger.getBasePackage()))
                .paths(PathSelectors.regex(swagger.getDefaultIncludePattern()))
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title(swagger.getTitle())
                .description(swagger.getDescription())
                .license(swagger.getLicense())
                .licenseUrl(swagger.getLicenseUrl())
                .termsOfServiceUrl(swagger.getTermsOfServiceUrl())
                .contact(new Contact(swagger.getContactName(), swagger.getContactUrl(), swagger.getContactUrl()))
                .version(swagger.getVersion())
                .build();
    }
}
