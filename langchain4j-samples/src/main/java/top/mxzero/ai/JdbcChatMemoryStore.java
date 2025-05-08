package top.mxzero.ai;

import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.ChatMessageType;
import dev.langchain4j.store.memory.chat.ChatMemoryStore;
import dev.langchain4j.store.memory.chat.InMemoryChatMemoryStore;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author Peng
 * @since 2025/4/23
 */
@Slf4j
public class JdbcChatMemoryStore implements ChatMemoryStore {
    private final ChatMemoryStore chatMemoryStore;

    public JdbcChatMemoryStore() {
        this.chatMemoryStore = new InMemoryChatMemoryStore();
    }

    @Override
    public List<ChatMessage> getMessages(Object o) {
        List<ChatMessage> messages = this.chatMemoryStore.getMessages(o);
        log.info("getMessages:{}, cnt:{}", o, messages.size());
        return messages;
    }

    @Override
    public void updateMessages(Object o, List<ChatMessage> list) {
        log.info("updateMessages:{}", o);
        ChatMessage newMessage = list.get(list.size() - 1);
        ChatMessageType type = newMessage.type();
        log.info("newMsg:{}", newMessage);
        this.chatMemoryStore.updateMessages(o, list);
    }

    @Override
    public void deleteMessages(Object o) {
        log.info("deleteMessages:{}", o);
        this.chatMemoryStore.deleteMessages(o);
    }
}
