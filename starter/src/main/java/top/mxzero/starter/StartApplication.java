package top.mxzero.starter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import top.mxzero.common.filter.RequestTraceFilter;
import top.mxzero.security.core.annotations.EnableSecurityConfig;

/**
 * @author Peng
 * @since 2025/2/15
 */
@EnableSecurityConfig
@SpringBootApplication
public class StartApplication {
    public static void main(String[] args) {
        SpringApplication.run(StartApplication.class, args);
    }


    @Bean
    public FilterRegistrationBean<RequestTraceFilter> requestTraceFilterFilterRegistrationBean() {
        FilterRegistrationBean<RequestTraceFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new RequestTraceFilter());
        registration.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return registration;
    }
}
