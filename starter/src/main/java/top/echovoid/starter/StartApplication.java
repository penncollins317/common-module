package top.echovoid.starter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import top.echovoid.security.core.annotations.EnableSecurityConfig;

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

   
}
