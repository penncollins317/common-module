package top.echovoid.tts.translate;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import top.echovoid.tts.translate.config.TTSProperties;
import top.echovoid.tts.translate.service.TTSService;
import top.echovoid.tts.translate.service.impl.YouDaoAiTTSService;

/**
 * @author Peng
 * @since 2025/4/23
 */
@EnableConfigurationProperties(TTSProperties.class)
@Configuration
@ComponentScan
public class TTSAutoConfig {
    @Bean
    public TTSService youDaoAiTTSService(TTSProperties properties) {
        return new YouDaoAiTTSService(properties);
    }
}
