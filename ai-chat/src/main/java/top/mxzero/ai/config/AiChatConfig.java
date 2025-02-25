package top.mxzero.ai.config;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import top.mxzero.ai.websocket.AiChatWebsocket;

/**
 * @author Peng
 * @since 2025/2/20
 */
@Configuration
@AllArgsConstructor
public class AiChatConfig implements WebSocketConfigurer {
    private final AiChatWebsocket aiChatWebsocket;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(this.aiChatWebsocket, "/websocket/ai/chat").setAllowedOrigins("*");
    }
}
