package top.echovoid.ai;

import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import top.echovoid.ai.config.DatabasePersistentChatMemory;
import top.echovoid.chat.mapper.AiChatMessageMapper;

/**
 * @author Penn Collins
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