package top.mxzero.security.oauth2.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.mxzero.common.dto.RestData;

/**
 * @author Peng
 * @since 2025/4/5
 */
@RestController
public class MessageController {
    @PreAuthorize("hasAuthority('SCOPE_message:read')")
    @RequestMapping("/message/read")
    public RestData<String> messageReadApi() {

        return RestData.success("message read");
    }

    @PreAuthorize("hasAuthority('SCOPE_message:write')")
    @RequestMapping("/message/write")
    public RestData<String> messageWriteApi() {
        return RestData.success("message write");
    }
}
