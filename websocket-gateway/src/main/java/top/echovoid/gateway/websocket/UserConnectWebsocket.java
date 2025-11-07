package top.echovoid.gateway.websocket;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import top.echovoid.gateway.websocket.dto.WebsocketMessageDTO;

import java.io.IOException;
import java.security.Principal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Penn Collins
 * @since 2025/11/7
 */
@Slf4j
public class UserConnectWebsocket extends TextWebSocketHandler {
    private final ObjectMapper objectMapper;

    public UserConnectWebsocket(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public static final Map<String, WebSocketSession> SESSION_MAP = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        Principal principal = session.getPrincipal();
        if (principal == null) {
            session.close();
            return;
        }
        log.info("{} websocket connection established", principal.getName());
        WebSocketSession existsWebsocket = SESSION_MAP.get(principal.getName());
        if (existsWebsocket != null) {
            existsWebsocket.close();
        }
        SESSION_MAP.put(principal.getName(), session);
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        Principal principal = session.getPrincipal();
        log.error("{} {} {}", principal.getName(), session.getId(), exception.getMessage());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        Principal principal = session.getPrincipal();
        if (principal != null) {
            log.info("{} websocket connection closed", principal.getName());
            SESSION_MAP.remove(principal.getName());
        }
    }


    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        try {
            WebsocketMessageDTO requestDTO = objectMapper.readValue(message.getPayload(), WebsocketMessageDTO.class);
            if ("echo".equals(requestDTO.getKey())) {
                sendMessage(session, requestDTO);
                return;
            }
            if ("send_to_user".equals(requestDTO.getKey())) {
                Map<String, String> sendData = objectMapper.readValue(requestDTO.getData(), new TypeReference<Map<String, String>>() {
                });
                String targetUser = sendData.get("target");
                String content = sendData.get("content");
                if (targetUser == null || content == null) {
                    return;
                }
                WebSocketSession targetSession = SESSION_MAP.get(targetUser);
                if (targetSession != null) {
                    WebsocketMessageDTO websocketResponseDTO = WebsocketMessageDTO.builder()
                            .requestId(requestDTO.getRequestId()).key("message_push").data(
                                    objectMapper.writeValueAsString(Map.of("from", session.getPrincipal().getName(), "data", content))
                            ).build();
                    sendMessage(targetSession, websocketResponseDTO);
                }
                return;
            }
            Principal principal = session.getPrincipal();
            log.info("user:{} => {}:{}", principal.getName(), requestDTO.getKey(), requestDTO.getData());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    public void sendMessage(WebSocketSession session, WebsocketMessageDTO messageDTO) {
        try {
            session.sendMessage(new TextMessage(objectMapper.writeValueAsString(messageDTO)));
        } catch (IOException ignored) {
        }
    }
}


