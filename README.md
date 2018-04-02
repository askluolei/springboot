# 基于 `spring-boot2` 的模版模版项目    

## 目标   
1. 代码即文档
2. 监控
3. 用户认证授权
4. 用户行为搜集
5. 审计
6. 日志级别动态修改
7. rest api 响应格式统一
8. 全局异常处理
9. 配置信息查看
10. 事件查看
11. 参数校验
12. 二维码生成
13. 常用工具页面
14. 敏感词过滤
15. 代码生成
16. 文件系统
17. 在线预览文件  
18. 邮件服务    
19. 爬虫
20. 代码生成插件

--- 


##代码即文档## 
使用 `swagger`    
```xml
    <dependency>
        <groupId>io.springfox</groupId>
        <artifactId>springfox-swagger2</artifactId>
        <version>${swagger.version}</version>
    </dependency>
    <dependency>
        <groupId>io.springfox</groupId>
        <artifactId>springfox-swagger-ui</artifactId>
        <version>${swagger.version}</version>
    </dependency>
``` 
依赖这两个   
再配置一下   
```java
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
``` 

一些基本配置都丢到配置文件里面去定义了 
然后不需要写任何东西，就可以看到api页面了 `http://localhost:8080/swagger-ui.html`  
如果还需要添加详细说明，则需要在代码上面添加特别的注解说明，不过会影响代码可读性    
也可以通过写 `api.yml` 文件，然后生成代码，这样的用法还得再研究一下，生成的代码可做参考   
```xml
    <plugin>
        <groupId>io.swagger</groupId>
        <artifactId>swagger-codegen-maven-plugin</artifactId>
        <version>${swagger-codegen-maven-plugin.version}</version>
        <executions>
            <execution>
                <goals>
                    <goal>generate</goal>
                </goals>
                <configuration>
                    <inputSpec>${project.basedir}/src/main/resources/swagger/api.yml</inputSpec>
                    <language>spring</language>
                    <apiPackage>com.luolei.template.web.api</apiPackage>
                    <modelPackage>com.luolei.template.web.api.model</modelPackage>
                    <generateSupportingFiles>false</generateSupportingFiles>
                    <configOptions>
                        <interfaceOnly>true</interfaceOnly>
                        <java8>true</java8>
                    </configOptions>
                </configuration>
            </execution>
        </executions>
    </plugin>
``` 
直接使用 `mvn generate-sources` 命令生成代码就行了   
文件可以在 `swagger` 官网上在线编辑，如果太慢，就用 `docker` 自己启动一个服务来编辑    
`compose` 文件在 `src/main/docker` 目录下，具体使用自己看 `docker`, `docker-compose`  

## 监控   

`spring-boot2` 默认使用 `micrometer` 实现监控    
主要用法跟`metrics` 的监控差不多~~

在方法层面主要使用注解 `@Timed` 添加执行时间指标，注意 `value` 必须要有值，否则无效    
其他类型的指标`Gauge` 之类的 可以通过实现 `MeterRegistryCustomizer` 或者 `MeterBinder` 来注册

```yml
management:
  endpoints:
    web:
      exposure:
        # 开启一些 http 接口 url 出来
        include: health,info,auditevents,beans,conditions,configprops,env,health,heapdump,httptrace,loggers,metrics,mappings,threaddump
``` 
也可以使用 通配符 `*` 

**micrometer监控**    
这是一个时序数据库，工作方式是通过配置文件（可以通过服务注册发现机制），定时到指定的 `url` 去获取指标信息    
然后可以查询数据，根据时间顺序有简陋的图表，也可以通过 `grafana` 获取里面的数据展示更炫酷的图标~~    
实例配置如下    
```yml
# 全局配置
global:
  scrape_interval: 15s # 默认 15秒到目标处抓取数据
  # 这个标签是在本机上每一条时间序列上都会默认产生的，主要可以用于 联合查询、远程存储、Alertmanger时使用。
  external_labels:
    monitor: 'codelab-monitor'
# 这里就表示抓取对象的配置
# 设置抓取自身数据
scrape_configs:
  # job name 这个配置是表示在这个配置内的时间序例，每一条都会自动添加上这个{job_name:"prometheus"}的标签。
  - job_name: 'prometheus'
    # 重写了全局抓取间隔时间，由15秒重写成5秒。
    # scrape_interval: 5s
    static_configs:
      - targets: ['localhost:9090']
  - job_name: 'spring'
    metrics_path: '/actuator/prometheus'
    static_configs:
      # 注意这里，本机跟容器的局域网ip
      - targets: ['10.0.75.1:8080']
```    

**grafana 集成**    
开发的时候使用 `docker` 来启动，正式环境怎么部署暂时不考虑    
```yml
version: "2"
services:
  # 数据搜集，时序数据库
  prometheus:
    image: prom/prometheus
    ports:
      - 9090:9090
    volumes:
      - ./../../volumn/prometheus/prometheus.yml:/etc/prometheus/prometheus.yml
  # 数据展示
  grafana:
    image: grafana/grafana
    ports:
      - 3000:3000
    environment:
      - GF_SECURITY_ADMIN_PASSWORD=admin
```    

然后就是在 `grafana` 里面配置数据源了（prometheus 的数据），然后就是展示    

**官网地址**    
[micrometer](http://micrometer.io)  
[prometheus](https://prometheus.io/)    
[grafana](https://grafana.com/) 

## 用户认证授权   

## 用户行为搜集   

## 审计   

## 日志级别动态修改     
这个很简单，只要把对应的 `url` 暴露出来就行了，然后就是画页面了 

## rest api 响应格式统一  
响应格式为   
```json
{
  "code": "success",
  "message": "成功",
  "data": string | object
} 

```
**实现**  
就是定义一个统一的返回对象   
```java
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class R {
    /**
     * 常见的响应吗常量定义在这里
     */
    public static final String SUCCESS = "success";
    public static final String FAIL = "fail";
    public static final String KNOWN_BIZ_ERROR = "known_biz_error";
    public static final String ILLEGAL_ARGUMENT = "illegal_argument";
    public static final String INTERNAL_ERROR = "internal_error";
    /**
     * 响应码对应的描述存在这里
     */
    public static final Map<String, String> codes = new HashMap<>();

    /**
     * 这里初始化响应码对应的描述
     * 后面也可以从文件读取
     */
    static {
        put(SUCCESS, "成功");
        put(FAIL, "失败");
        put(KNOWN_BIZ_ERROR, "业务异常");
        put(ILLEGAL_ARGUMENT, "请求参数不合法");
        put(INTERNAL_ERROR, "未预期的系统内部异常");
    }

    static void put(String key, String value) {
        codes.put(key, value);
    }

    // ------- 成功响应的快捷创建方式 响应码为 success
    public static R ok(Object data) {
        return new R(SUCCESS, codes.get(SUCCESS), data);
    }

    public static R ok(String message, Object data) {
        return new R(SUCCESS, message, data);
    }

    // ------- 失败响应的快捷创建方式 响应码为 自定义的
    public static R error(String code) {
        return new R(code, codes.get(code), null);
    }

    public static R error(String code, Object data) {
        return new R(code, codes.get(code), data);
    }

    public static R error(String code, String message, Object data) {
        return new R(code, message, data);
    }

    // ---------- 快速失败的快捷创建方式 这里的响应码是 fail
    public static R fail(Object data) {
        return new R(FAIL, codes.get(FAIL), data);
    }

    public static R fail(String message, Object data) {
        return new R(FAIL, message, data);
    }

    /**
     * 响应码
     * 不要使用 http 的状态码
     * 可以使用简练的英文
     */
    private String code;
    /**
     * 附带的描述信息，或者如果返回少量的数据可以在这里
     */
    private String message;
    /**
     * 响应数据
     */
    private Object data;

    public R code(String code) {
        this.code = code;
        if (codes.containsKey(code)) {
            this.message = codes.get(code);
        }
        return this;
    }

    public R message(String message) {
        this.message = message;
        return this;
    }

    public R data(Object data) {
        this.data = data;
        return this;
    }
}

```

**注意**  
尝试用 `aop` 查看响应是否是`R` 对象，如果不是就包装一下返回，但是，好像不能在 `aop` 中改变响应的类型，待继续尝试   

## 全局异常处理   
就是定义一个自定义业务异常基类，然后再继承改基类继续细分    
异常要继承 `RuntimeException` ，防止总是`try catch` 或者 `throws`   
```java
public class BaseException extends RuntimeException {

    public BaseException() {
        super();
    }

    public BaseException(String message) {
        super(message);
    }

    public BaseException(String message, Throwable cause) {
        super(message, cause);
    }

    public BaseException(Throwable cause) {
        super(cause);
    }
}
``` 
然后在全局异常处理   
```java
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
    /**
     * 处理自定义业务异常
     *
     * @param e
     * @param request
     * @return
     */
    @ExceptionHandler(BaseException.class)
    @ResponseBody
    public R handleEBException(Exception e, WebRequest request) {
        log.error("系统内部异常", e);
        if (isDebug(request)) {
            return R.error(KNOWN_BIZ_ERROR, e);
        } else {
            return R.error(KNOWN_BIZ_ERROR, e.getMessage());
        }
    }

    /**
     * 参数不合法异常
     *
     * @param e
     * @param request
     * @return
     */
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseBody
    public R handleIllegalArgument(Exception e, WebRequest request) {
        log.error("参数不合法", e);
        if (isDebug(request)) {
            return R.error(ILLEGAL_ARGUMENT, e);
        } else {
            return R.error(ILLEGAL_ARGUMENT, e.getMessage());
        }
    }

    /**
     * 其他未预期的异常
     *
     * @param e
     * @param request
     * @return
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public R handleAll(Exception e, WebRequest request) {
        log.error("未知异常", e);
        if (isDebug(request)) {
            return R.error(INTERNAL_ERROR, e);
        } else {
            return R.error(INTERNAL_ERROR, e.getMessage());
        }
    }

    private boolean isDebug(WebRequest request) {
        return Boolean.parseBoolean(request.getHeader("debug"));
    }
}
``` 
但是这只是处理`rest api` 的异常，其他响应的得另外处理，还有在 `spring` 体系里面出现的异常(譬如404)，要自己重写 `/error` 的处理方法    
 
## 配置信息查看
## 事件查看
## 参数校验
## 二维码生成
## 常用工具页面
## 敏感词过滤
## 代码生成
## 文件系统
## 在线预览文件  
## 邮件服务
## 爬虫   
## 代码生成插件