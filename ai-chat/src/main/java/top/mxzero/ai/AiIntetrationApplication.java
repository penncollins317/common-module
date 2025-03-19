package top.mxzero.ai;

import lombok.AllArgsConstructor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import top.mxzero.security.core.annotations.EnableSecurityConfig;

/**
 * @author Peng
 * @since 2024/11/27
 */
@EnableSecurityConfig
@MapperScan("top.mxzero.ai.mapper")
@AllArgsConstructor
@SpringBootApplication
public class AiIntetrationApplication {
    public static void main(String[] args) {
        SpringApplication.run(AiIntetrationApplication.class, args);
    }

    @Bean
    @ConditionalOnMissingBean(RestTemplate.class)
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}