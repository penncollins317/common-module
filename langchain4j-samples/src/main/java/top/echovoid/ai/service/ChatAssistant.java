package top.echovoid.ai.service;

import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.TokenStream;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.memory.ChatMemoryAccess;

/**
 * @author Penn Collins
 * @since 2025/4/23
 */
public interface ChatAssistant extends ChatMemoryAccess {
    String chat(String userMessage);

    String chat(@MemoryId String memoryId, @UserMessage String userMessage);

    TokenStream chatStream(String userMessage);

    TokenStream chatStream(@MemoryId String memoryId, @UserMessage String userMessage);

    TokenStream chatStream(@MemoryId int memoryId, @UserMessage String userMessage);
}
