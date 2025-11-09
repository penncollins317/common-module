package top.echovoid.gateway.websocket.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import top.echovoid.common.dto.RestData;
import top.echovoid.gateway.websocket.UserConnectWebsocket;
import top.echovoid.service.user.dto.UserinfoDTO;
import top.echovoid.service.user.service.UserService;

import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * websocket在线情况
 *
 * @author Penn Collins
 * @since 2025/11/8
 */
@AllArgsConstructor
@RestController
public class WebsocketOnlineUserController {
    private final UserService userService;

    /**
     * 在线用户
     */
    @GetMapping("/connect/onlines")
    public RestData<List<UserinfoDTO>> onlineUserList() {
        Set<Long> userIds = UserConnectWebsocket.SESSION_MAP.keySet();
        if (userIds.isEmpty()) {
            return RestData.ok(Collections.emptyList());
        }
        return RestData.ok(userService.getUserinfo(userIds));
    }
}
