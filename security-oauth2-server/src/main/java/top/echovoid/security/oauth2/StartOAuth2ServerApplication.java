package top.echovoid.security.oauth2;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Penn Collins
 * @since 2025/4/4
 */
@SpringBootApplication
@MapperScan("top.echovoid.security.oauth2.mapper")
public class StartOAuth2ServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(StartOAuth2ServerApplication.class, args);
    }
}
