package top.mxzero.ai;

import dev.langchain4j.store.memory.chat.ChatMemoryStore;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import top.mxzero.ai.config.OpenAiChatOptions;
import top.mxzero.ai.config.OpenAiConfig;
import top.mxzero.ai.controller.LangChain4jChatController;

/**
 * @author Peng
 * @since 2025/5/7
 */
@Configuration
@Import(LangChain4jChatController.class)
@EnableConfigurationProperties({OpenAiConfig.class, OpenAiChatOptions.class})
public class LangChainChatAutoConfig {

    @Bean
    public ChatMemoryStore chatMemoryStore() {
        return new JdbcChatMemoryStore();
    }
}
