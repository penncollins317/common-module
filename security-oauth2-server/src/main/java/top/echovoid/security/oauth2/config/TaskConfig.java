package top.echovoid.security.oauth2.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Penn Collins
 * @since 2025/4/5
 */
@EnableAsync
@Configuration
public class TaskConfig {
    @Bean(name = "taskExecutor")
    public ExecutorService taskExecutor() {
        return Executors.newFixedThreadPool(4);
    }
}
