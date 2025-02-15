package top.mxzero.starter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import top.mxzero.security.apikeys.annotations.EnableApiKeySecurityConfig;
import top.mxzero.security.core.annotations.EnableSecurityConfig;

/**
 * @author Peng
 * @since 2025/2/15
 */
@EnableSecurityConfig
@EnableApiKeySecurityConfig
@SpringBootApplication
public class StartApplication {
    public static void main(String[] args) {
        SpringApplication.run(StartApplication.class, args);
    }
}
