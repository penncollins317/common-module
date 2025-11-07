package top.echovoid.ai.service;

import org.springframework.http.codec.ServerSentEvent;
import reactor.core.publisher.Flux;
import top.echovoid.ai.dto.request.ChatInputDTO;
import top.echovoid.ai.dto.response.AiChatMessageDTO;
import top.echovoid.ai.dto.response.ConversationDTO;
import top.echovoid.common.params.PageParam;

import java.util.List;

/**
 * AI会话服务
 *
 * @author Penn Collins
 * @since 2025/4/19
 */
public interface AiChatService {
    /**
     * 创建新会话
     *
     * @param userId 用户ID
     * @param title  会话标题(可选)
     * @return 创建的会话信息
     */
    ConversationDTO createConversation(Long userId, String title);

    /**
     * 获取用户会话列表
     *
     * @param userId 用户ID
     * @param param  分页参数
     * @return 会话列表
     */
    List<ConversationDTO> listConversations(Long userId, PageParam param);

    /**
     * 删除会话
     *
     * @param conversationId 会话ID
     * @param userId         用户ID(用于权限验证)
     */
    void deleteConversation(String conversationId, Long userId);

    /**
     * 重命名会话
     *
     * @param conversationId 会话ID
     * @param userId         用户ID
     * @param newTitle       新标题
     * @return 是否修改成功
     */
    boolean renameConversation(String conversationId, Long userId, String newTitle);

    /**
     * 与AI进行对话
     *
     * @param input  对话输入
     * @param userId 用户ID
     * @return AI回复消息
     */
    String chat(ChatInputDTO input, Long userId);


    /**
     * 与AI进行对话，流式响应
     *
     * @param input  对话输入
     * @param userId 用户ID
     * @return 消息SSE
     */
    Flux<ServerSentEvent<Object>> chatSteam(ChatInputDTO input, Long userId);

    /**
     * 获取会话历史消息
     *
     * @param conversationId 会话ID
     * @param userId         用户ID(用于权限验证)
     * @param param          分页参数
     * @return 消息列表
     */
    List<AiChatMessageDTO> getConversationMessages(String conversationId, Long userId, PageParam param);

    /**
     * 清除会话消息
     *
     * @param conversationId 会话ID
     * @param userId         用户ID(用于权限验证)
     */
    void clearConversationMessages(String conversationId, Long userId);

    /**
     * 获取支持的AI模型列表
     *
     * @return 模型列表
     */
    List<String> getSupportedModels();
}
