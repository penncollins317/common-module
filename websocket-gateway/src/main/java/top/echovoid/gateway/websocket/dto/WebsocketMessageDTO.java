package top.echovoid.gateway.websocket.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Penn Collins
 * @since 2025/11/7
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WebsocketMessageDTO {
    private String requestId;
    private String key;
    private String data;
}
