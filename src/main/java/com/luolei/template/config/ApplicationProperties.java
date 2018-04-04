package com.luolei.template.config;

import com.luolei.template.support.DefaultValues;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;

import javax.validation.constraints.NotNull;

/**
 * 自定义的配置项
 *
 * @author luolei
 * @createTime 2018-03-24 12:39
 */
@Data
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {

    private final Async async = new Async();
    private final Swagger swagger = new Swagger();
    private final Metrics metrics = new Metrics();
    private FileSystem fileSystem = new FileSystem();
    private Security security = new Security();
    private final CorsConfiguration cors = new CorsConfiguration();
    private final Sequence sequence = new Sequence();
    private final Cache cache = new Cache();

    @Data
    public static class Sequence {
        private int workerId = DefaultValues.Sequence.workerId;
        private int datacenterId = DefaultValues.Sequence.datacenterId;
    }

    @Data
    public static class Async {
        private int corePoolSize = DefaultValues.Async.corePoolSize;
        private int maxPoolSize = DefaultValues.Async.maxPoolSize;
        private int queueCapacity = DefaultValues.Async.queueCapacity;
    }

    @Data
    public static class Swagger {
        private String title = DefaultValues.Swagger.title;
        private String description = DefaultValues.Swagger.description;
        private String version = DefaultValues.Swagger.version;
        private String termsOfServiceUrl = DefaultValues.Swagger.termsOfServiceUrl;
        private String contactName = DefaultValues.Swagger.contactName;
        private String contactUrl = DefaultValues.Swagger.contactUrl;
        private String contactEmail = DefaultValues.Swagger.contactEmail;
        private String license = DefaultValues.Swagger.license;
        private String licenseUrl = DefaultValues.Swagger.licenseUrl;
        private String defaultIncludePattern = DefaultValues.Swagger.defaultIncludePattern;
        private String host = DefaultValues.Swagger.host;
        private String[] protocols = DefaultValues.Swagger.protocols;
        private String basePackage = DefaultValues.Swagger.basePackage;
    }

    @Data
    public static class Metrics {
        private final Jmx jmx = new Jmx();
        private final Graphite graphite = new Graphite();
        private final Prometheus prometheus = new Prometheus();
        private final Logs logs = new Logs();

        @Data
        public static class Jmx {
            private boolean enabled = DefaultValues.Metrics.Jmx.enabled;
        }

        @Data
        public static class Graphite {
            private boolean enabled = DefaultValues.Metrics.Graphite.enabled;
            private String host = DefaultValues.Metrics.Graphite.host;
            private int port = DefaultValues.Metrics.Graphite.port;
            private String prefix = DefaultValues.Metrics.Graphite.prefix;
        }

        @Data
        public static class Prometheus {
            private boolean enabled = DefaultValues.Metrics.Prometheus.enabled;
            private String endpoint = DefaultValues.Metrics.Prometheus.endpoint;
        }

        @Data
        public static class Logs {
            private boolean enabled = DefaultValues.Metrics.Logs.enabled;
            private long reportFrequency = DefaultValues.Metrics.Logs.reportFrequency;
        }
    }

    @Data
    public static class FileSystem {
        private String rootDir;
    }

    @Data
    public static class Security {
        private final ClientAuthorization clientAuthorization = new ClientAuthorization();
        private final Authentication authentication = new Authentication();
        private final RememberMe rememberMe = new RememberMe();

        @Data
        public static class ClientAuthorization {
            private String accessTokenUri = DefaultValues.Security.ClientAuthorization.accessTokenUri;
            private String tokenServiceId = DefaultValues.Security.ClientAuthorization.tokenServiceId;
            private String clientId = DefaultValues.Security.ClientAuthorization.clientId;
            private String clientSecret = DefaultValues.Security.ClientAuthorization.clientSecret;
        }

        @Data
        public static class Authentication {
            private final Jwt jwt = new Jwt();

            @Data
            public static class Jwt {
                private String secret = DefaultValues.Security.Authentication.Jwt.secret;
                private long tokenValidityInSeconds = DefaultValues.Security.Authentication.Jwt.tokenValidityInSeconds;
                private long tokenValidityInSecondsForRememberMe = DefaultValues.Security.Authentication.Jwt.tokenValidityInSecondsForRememberMe;
            }
        }

        @Data
        public static class RememberMe {
            @NotNull
            private String key = DefaultValues.Security.RememberMe.key;
        }
    }

    @Data
    public static class Cache {
        private final Hazelcast hazelcast = new Hazelcast();
        private final Ehcache ehcache = new Ehcache();
        private final Infinispan infinispan = new Infinispan();

        @Data
        public static class Hazelcast {
            private int timeToLiveSeconds = DefaultValues.Cache.Hazelcast.timeToLiveSeconds;
            private int backupCount = DefaultValues.Cache.Hazelcast.backupCount;
            private final ManagementCenter managementCenter = new ManagementCenter();

            @Data
            public static class ManagementCenter {
                private boolean enabled = DefaultValues.Cache.Hazelcast.ManagementCenter.enabled;
                private int updateInterval = DefaultValues.Cache.Hazelcast.ManagementCenter.updateInterval;
                private String url =  DefaultValues.Cache.Hazelcast.ManagementCenter.url;
            }
        }

        @Data
        public static class Ehcache {
            private int timeToLiveSeconds = DefaultValues.Cache.Ehcache.timeToLiveSeconds;
            private long maxEntries = DefaultValues.Cache.Ehcache.maxEntries;
        }

        @Data
        public static class Infinispan {
            private String configFile = DefaultValues.Cache.Infinispan.configFile;
            private boolean statsEnabled = DefaultValues.Cache.Infinispan.statsEnabled;
            private final Local local = new Local();
            private final Distributed distributed = new Distributed();
            private final Replicated replicated = new Replicated();

            @Data
            public static class Local {
                private long timeToLiveSeconds = DefaultValues.Cache.Infinispan.Local.timeToLiveSeconds;
                private long maxEntries = DefaultValues.Cache.Infinispan.Local.maxEntries;
            }

            @Data
            public static class Distributed {
                private long timeToLiveSeconds = DefaultValues.Cache.Infinispan.Distributed.timeToLiveSeconds;
                private long maxEntries = DefaultValues.Cache.Infinispan.Distributed.maxEntries;
                private int instanceCount = DefaultValues.Cache.Infinispan.Distributed.instanceCount;
            }

            @Data
            public static class Replicated {
                private long timeToLiveSeconds = DefaultValues.Cache.Infinispan.Replicated.timeToLiveSeconds;
                private long maxEntries = DefaultValues.Cache.Infinispan.Replicated.maxEntries;
            }
        }
    }
}
