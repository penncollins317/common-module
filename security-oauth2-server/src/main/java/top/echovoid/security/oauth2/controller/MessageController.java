package top.echovoid.security.oauth2.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.echovoid.common.dto.RestData;

/**
 * @author Penn Collins
 * @since 2025/4/5
 */
@Tag(name = "消息接口 (OAuth2)", description = "受 OAuth2 权限控制的消息读写接口示例")
@RestController
public class MessageController {
    @Operation(summary = "读取消息", description = "演示具有 SCOPE_message:read 权限的用户读取消息")
    @PreAuthorize("hasAuthority('SCOPE_message:read')")
    @RequestMapping("/message/read")
    public RestData<String> messageReadApi() {

        return RestData.success("message read");
    }

    @Operation(summary = "写入消息", description = "演示具有 SCOPE_message:write 权限的用户写入消息")
    @PreAuthorize("hasAuthority('SCOPE_message:write')")
    @RequestMapping("/message/write")
    public RestData<String> messageWriteApi() {
        return RestData.success("message write");
    }
}
