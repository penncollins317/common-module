package top.echovoid.starter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import top.echovoid.security.core.SecurityConfigProvider;
import top.echovoid.security.core.annotations.EnableSecurityConfig;

import java.util.Set;

/**
 * @author Penn Collins
 * @since 2025/2/15
 */
@EnableSecurityConfig
@SpringBootApplication
public class StartApplication {
    public static void main(String[] args) {
        SpringApplication.run(StartApplication.class, args);
    }

    @Bean
    public SecurityConfigProvider securityConfigProvider() {
        return new SecurityConfigProvider() {
            @Override
            public Set<String> ignoreUrls() {
                return Set.of("/actuator/**");
            }
        };
    }
}
