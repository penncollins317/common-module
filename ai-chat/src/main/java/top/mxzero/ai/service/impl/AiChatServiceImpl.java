package top.mxzero.ai.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.PromptChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import top.mxzero.ai.dto.request.ChatInputDTO;
import top.mxzero.ai.dto.request.CreateConversationDTO;
import top.mxzero.ai.dto.response.AiChatMessageDTO;
import top.mxzero.ai.dto.response.ConversationDTO;
import top.mxzero.chat.entity.AiChatMessage;
import top.mxzero.chat.entity.AiConversation;
import top.mxzero.chat.mapper.AiChatMessageMapper;
import top.mxzero.chat.mapper.AiConversationMapper;
import top.mxzero.ai.service.AiChatService;
import top.mxzero.ai.service.AiConversationService;
import top.mxzero.common.exceptions.ServiceException;
import top.mxzero.common.params.PageParam;
import top.mxzero.common.utils.DeepBeanUtil;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Peng
 * @since 2025/4/19
 */
@Slf4j
@Service
public class AiChatServiceImpl implements AiChatService {
    private final ChatModel chatModel;
    private final ChatMemory chatMemory;
    private final ChatClient client;
    private final AiChatMessageMapper chatMessageMapper;
    private final AiConversationService conversationService;
    private final AiConversationMapper conversationMapper;

    public AiChatServiceImpl(
            ChatModel chatModel, ChatMemory chatMemory,
            AiConversationService conversationService, AiChatMessageMapper chatMessageMapper,
            AiConversationMapper conversationMapper
    ) {
        this.conversationMapper = conversationMapper;
        this.conversationService = conversationService;
        this.chatMessageMapper = chatMessageMapper;
        this.chatModel = chatModel;
        this.chatMemory = chatMemory;
        this.client = this.buildChatClient(this.chatModel);
    }

    private ChatClient buildChatClient(ChatModel chatModel) {
        return ChatClient.builder(chatModel).defaultAdvisors(new PromptChatMemoryAdvisor(this.chatMemory))
                .build();
    }

    @Override
    public ConversationDTO createConversation(Long userId, String title) {
        String conversationId = this.conversationService.create(new CreateConversationDTO(title, userId));
        Date current = new Date();
        return new ConversationDTO(conversationId, title, userId, current, current);
    }

    @Override
    public List<ConversationDTO> listConversations(Long userId, PageParam param) {
        return this.conversationService.listConversation(userId, null);
    }

    @Override
    public void deleteConversation(String conversationId, Long userId) {
        boolean deleteSuccess = this.conversationService.deleteConversation(conversationId, userId);
        if (deleteSuccess) {
            this.chatMemory.clear(conversationId);
        }
    }

    @Override
    public boolean renameConversation(String conversationId, Long userId, String newTitle) {
        ConversationDTO conversationDTO = this.conversationService.getById(conversationId);
        if (conversationDTO == null) {
            throw new ServiceException("会话ID不存在");
        }
        if (userId != null && !conversationDTO.getUserId().equals(userId) || userId == null && conversationDTO.getUserId() != null) {
            throw new AccessDeniedException("无权访问");
        }
        AiConversation aiConversation = new AiConversation();
        aiConversation.setConversationId(conversationId);
        aiConversation.setTitle(newTitle);
        return this.conversationMapper.updateById(aiConversation) > 0;
    }

    @Override
    public String chat(ChatInputDTO input, Long userId) {
        OpenAiChatOptions.Builder optionsBuilder = OpenAiChatOptions.builder()
                .temperature(input.isThinkingEnabled() ? Double.valueOf(0.3) : this.chatModel.getDefaultOptions().getTemperature());
        ConversationDTO conversationDTO = this.conversationService.getById(input.getConversationId());
        if (conversationDTO == null) {
            throw new ServiceException("会话ID不存在");
        }

        if (userId != null && !conversationDTO.getUserId().equals(userId) || userId == null && conversationDTO.getUserId() != null) {
            throw new AccessDeniedException("无权访问");
        }

        if (StringUtils.hasLength(input.getModel())) {
            optionsBuilder.model(input.getModel());
        } else {
            optionsBuilder.model(this.chatModel.getDefaultOptions().getModel());
        }
        Prompt prompt = new Prompt(input.getContent(), optionsBuilder.build());
        ChatResponse response = this.client.prompt(prompt)
                .advisors(advisorSpec -> {
                    advisorSpec.param("chat_memory_conversation_id", input.getConversationId());
                })
                .call().chatResponse();
        if (response == null) {
            log.error("AI响应为空");
            throw new IllegalStateException("服务器繁忙，请稍后再试。");
        }
        return response.getResult().getOutput().getText();
    }

    @Override
    public Flux<ServerSentEvent<Object>> chatSteam(ChatInputDTO input, Long userId) {
        OpenAiChatOptions.Builder optionsBuilder = OpenAiChatOptions.builder()
                .temperature(input.isThinkingEnabled() ? Double.valueOf(0.3) : this.chatModel.getDefaultOptions().getTemperature());

        ConversationDTO conversationDTO = this.conversationService.getById(input.getConversationId());
        if (conversationDTO == null) {
            throw new ServiceException("会话ID不存在");
        }
        if (userId != null && !conversationDTO.getUserId().equals(userId) || userId == null && conversationDTO.getUserId() != null) {
            throw new AccessDeniedException("无权访问");
        }

        if (StringUtils.hasLength(input.getModel())) {
            optionsBuilder.model(input.getModel());
        } else {
            optionsBuilder.model(this.chatModel.getDefaultOptions().getModel());
        }
        Prompt prompt = new Prompt(input.getContent(), optionsBuilder.build());

        return this.client.prompt(prompt)
                .advisors(advisorSpec -> {
                    advisorSpec.param("chat_memory_conversation_id", input.getConversationId());
                })
                .stream()
                .chatResponse()
                .map(data -> {
                    Map<String, Object> responseData = new HashMap<>();
                    responseData.put("v", data.getResult().getOutput().getText());
                    return ServerSentEvent.builder()
                            .event("message")
                            .data(responseData)
                            .build();
                });
    }

    @Override
    public List<AiChatMessageDTO> getConversationMessages(String conversationId, Long userId, PageParam param) {
        ConversationDTO conversationDTO = this.conversationService.getById(conversationId);
        if (conversationDTO == null) {
            throw new ServiceException("会话ID不存在");
        }
        if (userId != null && !conversationDTO.getUserId().equals(userId) || userId == null && conversationDTO.getUserId() != null) {
            throw new AccessDeniedException("无权访问");
        }

        Page<AiChatMessage> page = this.chatMessageMapper.selectPage(new Page<>(param.getPage(), param.getSize()), new QueryWrapper<AiChatMessage>().eq("conversation_id", conversationId));
        return DeepBeanUtil.copyProperties(page.getRecords(), AiChatMessageDTO::new);
    }

    @Override
    public void clearConversationMessages(String conversationId, Long userId) {
        ConversationDTO conversationDTO = this.conversationService.getById(conversationId);
        if (conversationDTO == null) {
            throw new ServiceException("会话ID不存在");
        }
        if (userId != null && !conversationDTO.getUserId().equals(userId) || userId == null && conversationDTO.getUserId() != null) {
            throw new AccessDeniedException("无权访问");
        }
        this.chatMemory.clear(conversationId);
    }

    @Override
    public List<String> getSupportedModels() {
        return List.of("qwen-plus", "qwen-max", "deepseek-v3", "deepseek-r1");
    }
}
