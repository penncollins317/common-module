package top.echovoid.message.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.echovoid.common.annotations.HasScope;

import java.security.Principal;

/**
 * @author Peng
 * @since 2025/4/12
 */
@RestController
public class MessageController {
    @HasScope("message.read")
    @RequestMapping("/message")
    public String echoMessage(String msg) {
        return "【ECHO】" + msg;
    }

    @HasScope("message.write")
    @RequestMapping("/message/write")
    public String echoMessagePermission(Principal principal) {
        return "message write";
    }

    @HasScope("openapi")
    @RequestMapping("/openapi")
    public String openApi() {
        return "openapi";
    }
}
