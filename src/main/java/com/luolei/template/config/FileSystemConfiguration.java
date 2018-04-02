package com.luolei.template.config;

import com.luolei.template.support.FileSystem;
import com.luolei.template.support.LocalFileSystem;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author luolei
 * @createTime 2018-03-31 11:00
 */
@Configuration
public class FileSystemConfiguration {

    private final ApplicationProperties applicationProperties;

    public FileSystemConfiguration(ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
    }

    @ConditionalOnMissingBean(FileSystem.class)
    @Bean
    public FileSystem fileSystem() {
        return new LocalFileSystem(applicationProperties);
    }
}
