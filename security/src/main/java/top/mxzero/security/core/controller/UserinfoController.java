package top.mxzero.security.core.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.mxzero.security.core.dto.UserinfoDTO;
import top.mxzero.security.core.service.UserService;

import java.security.Principal;

/**
 * @author Peng
 * @since 2025/1/11
 */
@AllArgsConstructor
@RestController
public class UserinfoController {
    private final UserService userService;

    @RequestMapping("/api/userinfo")
    public UserinfoDTO userinfoApi(Principal principal) {
        return this.userService.getUserinfo(Long.valueOf(principal.getName()));
    }
}
