package top.mxzero.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import top.mxzero.common.api.IpQueryApi;
import top.mxzero.common.config.props.IpQueryProp;
import top.mxzero.common.config.props.YunTongXunSmsProps;
import top.mxzero.common.filter.RequestTraceFilter;
import top.mxzero.common.service.EmailSender;
import top.mxzero.common.service.SmsSender;
import top.mxzero.common.service.impl.ConsoleSmsSender;
import top.mxzero.common.service.impl.YunTongXinSmsSender;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Peng
 * @email qianmeng6879@163.com
 * @since 2024/3/21
 */
@Slf4j
@Configuration
@EnableAsync
@ComponentScan
public class CommonAutoConfig {

    @Bean
    public FilterRegistrationBean<RequestTraceFilter> requestTraceFilterFilterRegistrationBean() {
        FilterRegistrationBean<RequestTraceFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new RequestTraceFilter());
        registration.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return registration;
    }
//    @Override
//    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
//        configurer
//                .favorParameter(true)
//                .parameterName("format")
//                .mediaType("json", MediaType.APPLICATION_JSON)
//                .mediaType("xml", MediaType.APPLICATION_XML)
//                .defaultContentType(MediaType.APPLICATION_JSON);
//    }


    @Bean
    @ConfigurationProperties("mxzero.api.ip.key")
    public IpQueryProp ipQueryProp() {
        return new IpQueryProp();
    }

    @Bean
    @ConditionalOnBean(IpQueryProp.class)
    public IpQueryApi ipQueryApi(IpQueryProp prop, RestTemplate restTemplate) {
        return new IpQueryApi(prop.getKey(), restTemplate);
    }

    @Bean
    @ConditionalOnMissingBean(RestTemplate.class)
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public EmailSender emailSender() {
        return new EmailSender();
    }


    @Bean
    @ConditionalOnProperty("mxzero.sms.yuntongxun")
    public YunTongXunSmsProps yunTongXunSmsProps() {
        return new YunTongXunSmsProps();
    }

    @Bean
    @ConditionalOnBean(YunTongXunSmsProps.class)
    public SmsSender smsSender(YunTongXunSmsProps smsProps) {
        return new YunTongXinSmsSender(smsProps);
    }

    @Bean
    @ConditionalOnMissingBean(SmsSender.class)
    public SmsSender consoleSmsSender() {
        return new ConsoleSmsSender();
    }


    @Bean
    @ConditionalOnMissingBean(ExecutorService.class)
    public ExecutorService commonThreadPoll() {
        return Executors.newSingleThreadExecutor();
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
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE); // 设置优先级为最高
        return bean;
    }
}
