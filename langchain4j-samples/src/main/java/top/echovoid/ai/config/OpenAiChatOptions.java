package top.echovoid.ai.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Penn Collins
 * @since 2025/5/7
 */
@Data
@ConfigurationProperties("echovoid.openai.chat")
public class OpenAiChatOptions {
    private String model;
    private double temperature = 1.0F;
    private int maxHistoryMsg = 30;
}