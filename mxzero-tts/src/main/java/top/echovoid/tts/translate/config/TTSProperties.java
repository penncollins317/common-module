package top.echovoid.tts.translate.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Penn Collins
 * @since 2025/4/23
 */
@Data
@ConfigurationProperties(prefix = "echovoid.tts")
public class TTSProperties {
    private String appKey;
    private String appSecret;
}
