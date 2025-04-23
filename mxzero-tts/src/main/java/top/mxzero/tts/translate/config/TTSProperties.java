package top.mxzero.tts.translate.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Peng
 * @since 2025/4/23
 */
@Data
@ConfigurationProperties(prefix = "mxzero.tts")
public class TTSProperties {
    private String appKey;
    private String appSecret;
}
