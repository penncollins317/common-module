package top.mxzero.ai;

import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.AiServices;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.mxzero.ai.service.ChatAssistant;
import top.mxzero.ai.service.TimeService;

/**
 * @author Peng
 * @since 2025/4/23
 */
public class LangChainChatModelTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(LangChainChatModelTest.class);


    @Test
    public void testChatModel() {

        OpenAiChatModel chatModel = OpenAiChatModel.builder()
                .apiKey(Const.OPENAI_API_KEY)
                .baseUrl(Const.OPENAI_BASE_URL)
                .modelName(Const.CHAT_MODEL_NAME)
                .temperature(0.7)
                .build();
        ChatAssistant assistant = AiServices.builder(ChatAssistant.class)
                .chatLanguageModel(chatModel)
                .tools(new TimeService())
                .chatMemoryProvider(memoryId -> MessageWindowChatMemory.builder()
                        .id(memoryId)
                        .maxMessages(10)
                        .chatMemoryStore(new JdbcChatMemoryStore())
                        .build())
                .build();

        //
        String responseText2 = assistant.chat("现在系统是什么时间");
        LOGGER.info(responseText2);
//        String responseText3 = assistant.chat("你确定是2023年时间？仔细想一下呢");
//        LOGGER.info(responseText3);
//        CountDownLatch countDownLatch = new CountDownLatch(1);
//        chatModel.chat("你好，你可以做什么", new StreamingChatResponseHandler() {
//
//            @Override
//            public void onPartialResponse(String s) {
//                LOGGER.info(s);
//            }
//
//            @Override
//            public void onCompleteResponse(ChatResponse chatResponse) {
//                LOGGER.info("completed:{}", chatResponse.aiMessage().text());
//                countDownLatch.countDown();
//            }
//
//            @Override
//            public void onError(Throwable throwable) {
//                LOGGER.error(throwable.getMessage());
//                countDownLatch.countDown();
//            }
//        });
//        try {
//            countDownLatch.await();
//        } catch (Exception e) {
//            LOGGER.error(e.getMessage());
//        }
    }
}
