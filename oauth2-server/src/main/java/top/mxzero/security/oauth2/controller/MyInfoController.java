package top.mxzero.security.oauth2.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import top.mxzero.security.core.dto.UserinfoDTO;
import top.mxzero.security.core.service.UserService;

import java.security.Principal;

/**
 * @author Peng
 * @since 2025/3/31
 */
@Slf4j
@Controller
@AllArgsConstructor
public class MyInfoController {
    private UserService userService;
    @RequestMapping("/")
    public String indexPage(Principal principal, Model model) {
        UserinfoDTO userinfo = userService.getUserinfoByUsername(principal.getName());
        log.info("user:{}", userinfo);
        model.addAttribute("user", userinfo);
        return "my";
    }
}
