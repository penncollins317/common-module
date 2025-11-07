package top.echovoid.ai.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Peng
 * @since 2025/5/7
 */
@Data
@ConfigurationProperties("mxzero.openai.chat")
public class OpenAiChatOptions {
    private String model;
    private double temperature = 1.0F;
    private int maxHistoryMsg = 30;
}