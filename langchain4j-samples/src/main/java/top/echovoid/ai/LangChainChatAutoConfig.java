package top.echovoid.ai;

import dev.langchain4j.store.memory.chat.ChatMemoryStore;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import top.echovoid.ai.config.OpenAiChatOptions;
import top.echovoid.ai.config.OpenAiConfig;
import top.echovoid.ai.controller.LangChain4jChatController;

/**
 * @author Peng
 * @since 2025/5/7
 */
@Configuration
@MapperScan("top.echovoid.ai.mapper")
@Import(LangChain4jChatController.class)
@EnableConfigurationProperties({OpenAiConfig.class, OpenAiChatOptions.class})
public class LangChainChatAutoConfig {

    @Bean
    public ChatMemoryStore chatMemoryStore() {
        return new JdbcChatMemoryStore();
    }
}
