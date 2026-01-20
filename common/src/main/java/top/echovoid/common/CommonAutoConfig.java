package top.echovoid.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import top.echovoid.common.filter.RequestTraceFilter;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;

/**
 * @author Penn Collins
 * @email penncollins317@gmail.com
 * @since 2024/3/21
 */
@Slf4j
@Configuration
@ComponentScan
public class CommonAutoConfig {

    @Bean
    public FilterRegistrationBean<RequestTraceFilter> requestTraceFilterFilterRegistrationBean() {
        FilterRegistrationBean<RequestTraceFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new RequestTraceFilter());
        registration.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return registration;
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowedOriginPatterns(Collections.singletonList("*")); // 允许所有来源
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE")); // 允许的请求方法
        config.setAllowedHeaders(Collections.singletonList("*")); // 允许所有请求头
        config.setAllowCredentials(true); // 允许发送凭据信息
        config.setMaxAge(Duration.ofSeconds(3600)); // 缓存预检请求的时间，单位为秒

        source.registerCorsConfiguration("/**", config); // 对所有路径生效

        return new CorsFilter(source);
    }

    @Bean
    public FilterRegistrationBean<CorsFilter> filterFilterRegistrationBean() {
        FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<>(corsFilter());
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return bean;
    }

}
