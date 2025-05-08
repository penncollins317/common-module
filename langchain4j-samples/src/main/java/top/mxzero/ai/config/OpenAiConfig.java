package top.mxzero.ai.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Peng
 * @since 2025/5/7
 */
@Data
@ConfigurationProperties("mxzero.openai")
public class OpenAiConfig {
    private String baseUrl;
    private String apiKey;
}