package top.mxzero.security.oauth2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

/**
 * @author Peng
 * @since 2025/3/31
 */
@EntityScan("top.mxzero.security.oauth2.entity")
@SpringBootApplication
public class StartOAuth2Application {
    public static void main(String[] args) {
        SpringApplication.run(StartOAuth2Application.class, args);
    }
}
