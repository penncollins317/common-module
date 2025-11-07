package top.mxzero.gateway.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 * @author Peng
 * @since 2025/11/7
 */
@Configuration
@EnableWebSocket
public class WebSocketGatewayAutoConfig implements WebSocketConfigurer {
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new UserConnectWebsocket(objectMapper), "/websocket/user/connect").setAllowedOriginPatterns("*");
    }
}
