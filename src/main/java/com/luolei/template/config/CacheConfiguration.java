package com.luolei.template.config;

import com.luolei.template.support.Constants;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.ExpiryPolicyBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.jsr107.Eh107Configuration;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

/**
 * 缓存配置
 *
 * @author 罗雷
 * @date 2018/4/4 0004
 * @time 13:32
 */
@Configuration
@EnableCaching
public class CacheConfiguration {

    private final javax.cache.configuration.Configuration<String, String> jcacheConfiguration;

    public CacheConfiguration(ApplicationProperties applicationProperties) {
        ApplicationProperties.Cache.Ehcache ehcache =
                applicationProperties.getCache().getEhcache();
        jcacheConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(
                CacheConfigurationBuilder.newCacheConfigurationBuilder(String.class, String.class,
                        ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                        .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                        .build());
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            cm.createCache(Constants.CACHE_TOKEN_INVALID, jcacheConfiguration);
        };
    }
}
