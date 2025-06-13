package top.mxzero.ai;

import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import top.mxzero.ai.config.DatabasePersistentChatMemory;
import top.mxzero.chat.mapper.AiChatMessageMapper;

/**
 * @author Peng
 * @since 2024/11/27
 */
@Configuration
@ComponentScan
public class AiIntetrationAutoConfig {
    @Bean
    public ChatMemory chatMemory(AiChatMessageMapper chatMessageMapper) {
        return new DatabasePersistentChatMemory(chatMessageMapper);
    }
}