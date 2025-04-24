package top.mxzero.ai.service;

import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.UserMessage;

/**
 * @author Peng
 * @since 2025/4/23
 */
public interface ChatAssistant {
    String chat(String userMessage);

    String chat(@MemoryId String memoryId, @UserMessage String userMessage);
}
