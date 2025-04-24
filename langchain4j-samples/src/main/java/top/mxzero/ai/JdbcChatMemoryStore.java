package top.mxzero.ai;

import dev.langchain4j.data.message.ChatMessage;
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
        log.info("getMessages:{}", o);
        return this.chatMemoryStore.getMessages(o);
    }

    @Override
    public void updateMessages(Object o, List<ChatMessage> list) {
        log.info("updateMessages:{}", o);
        list.forEach(item->{
            log.info("updateMessages item:{}", item);
        });
        this.chatMemoryStore.updateMessages(o, list);
    }

    @Override
    public void deleteMessages(Object o) {
        log.info("deleteMessages:{}", o);
        this.chatMemoryStore.deleteMessages(o);
    }
}
