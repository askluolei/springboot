package com.luolei.template.support;

import static com.luolei.template.support.Constants.SPRING_PROFILE_DEVELOPMENT;

/**
 * 应用中的一些默认值
 *
 * @author luolei
 * @createTime 2018-03-24 12:14
 */
public interface DefaultValues {

    String SPRING_PROFILE_DEFAULT =SPRING_PROFILE_DEVELOPMENT;

    /**
     * 异步线程池的配置常量
     */
    interface Async {
        int corePoolSize = 2;
        int maxPoolSize = 50;
        int queueCapacity = 10000;
    }

    /**
     * swagger 的默认配置
     */
    interface Swagger {
        String title = "Application API";
        String description = "API documentation";
        String version = "0.0.1";
        String termsOfServiceUrl = null;
        String contactName = null;
        String contactUrl = null;
        String contactEmail = null;
        String license = null;
        String licenseUrl = null;
        String defaultIncludePattern = "/.*";
        String host = null;
        String[] protocols = {};
        String basePackage = "com.luolei.template.web";
    }

    /**
     * metrics 监控相关
     */
    interface Metrics {
        interface Jmx {
            boolean enabled = true;
        }

        interface Graphite {
            boolean enabled = false;
            String host = "localhost";
            int port = 2003;
            String prefix = "jhipsterApplication";
        }

        interface Prometheus {
            boolean enabled = false;
            String endpoint = "/prometheusMetrics";
        }

        interface Logs {
            boolean enabled = true;
            long reportFrequency = 60;

        }
    }

    /**
     * 安全相关
     */
    interface Security {
        interface ClientAuthorization {
            String accessTokenUri = null;
            String tokenServiceId = null;
            String clientId = null;
            String clientSecret = null;
        }

        interface Authentication {
            interface Jwt {
                String secret = null;
                long tokenValidityInSeconds = 1800; // 0.5 hour
                long tokenValidityInSecondsForRememberMe = 2592000; // 30 hours;
            }
        }

        interface RememberMe {
            String key = null;
        }
    }
}
