package com.luolei.template.config;

import com.luolei.template.aop.AuthorityAspect;
import com.luolei.template.security.SecurityProblemSupport;
import com.luolei.template.security.jwt.JWTConfigurer;
import com.luolei.template.security.jwt.TokenProvider;
import com.luolei.template.support.Constants;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;

import javax.annotation.PostConstruct;
import javax.cache.CacheManager;

/**
 * @author 罗雷
 * @date 2018/4/3 0003
 * @time 16:21
 */
@Configuration
@Import(SecurityProblemSupport.class)
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    private final UserDetailsService userDetailsService;
    private final TokenProvider tokenProvider;
    private final CorsFilter corsFilter;
    private final SecurityProblemSupport problemSupport;

    public SecurityConfiguration(AuthenticationManagerBuilder authenticationManagerBuilder, UserDetailsService userDetailsService,TokenProvider tokenProvider,CorsFilter corsFilter, SecurityProblemSupport problemSupport) {
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.userDetailsService = userDetailsService;
        this.tokenProvider = tokenProvider;
        this.corsFilter = corsFilter;
        this.problemSupport = problemSupport;
    }

    private CacheManager cacheManager;

    @Autowired
    public void setCacheManager(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    /**
     * 初始化
     */
    @PostConstruct
    public void init() {
        try {
            authenticationManagerBuilder
                    .userDetailsService(userDetailsService)
                    .passwordEncoder(passwordEncoder());
        } catch (Exception e) {
            throw new BeanInitializationException("Security configuration failed", e);
        }
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /**
     * 密码hash类
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 自定义安全注解 AOP 配置
     * @return
     */
    @Bean
    public AuthorityAspect authorityAspect() {
        return new AuthorityAspect();
    }

    /**
     * 配置忽略安全的url
     * 这里的配置优先
     * 下面的 httpsecurity 配置优先级在后面
     * @param web
     * @throws Exception
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .antMatchers(HttpMethod.OPTIONS, "/**")
                .antMatchers("/app/**/*.{js,html}")
                .antMatchers("/i18n/**")
                .antMatchers("/content/**")
//                .antMatchers("/test/**")
                .mvcMatchers("/swagger-ui.html")
                .antMatchers("/h2-console/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling()
                .authenticationEntryPoint(problemSupport)
                .accessDeniedHandler(problemSupport)
                .and()
                .csrf()
                .disable()
                .headers()
                .frameOptions()
                .disable()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/test/**").permitAll()
//                .antMatchers("/api/register").permitAll()
//                .antMatchers("/api/activate").permitAll()
                .antMatchers("/api/authenticate").permitAll()
//                .antMatchers("/api/account/reset-password/init").permitAll()
//                .antMatchers("/api/account/reset-password/finish").permitAll()
//                .antMatchers("/api/profile-info").permitAll()
                .antMatchers("/api/**").authenticated()
//                .antMatchers("/websocket/tracker").hasAuthority(Constants.ROLE_ADMIN)
//                .antMatchers("/websocket/**").permitAll()
//                .antMatchers("/management/health").permitAll()
//                .antMatchers("/management/**").hasAuthority(Constants.ROLE_ADMIN)
//                .antMatchers("/v2/api-docs/**").permitAll()
//                .antMatchers("/swagger-resources/configuration/ui").permitAll()
//                .antMatchers("/swagger-ui.html").hasAuthority(Constants.ROLE_ADMIN)
                .mvcMatchers("/swagger-ui.html").hasRole(Constants.ROLE_ADMIN)// 针对页面的要用 mvcMatchers , rest接口用 antMatchers
                .and()
                .apply(securityConfigurerAdapter());

    }

    private JWTConfigurer securityConfigurerAdapter() {
        return new JWTConfigurer(tokenProvider, cacheManager.getCache(Constants.CACHE_TOKEN_INVALID));
    }

}
