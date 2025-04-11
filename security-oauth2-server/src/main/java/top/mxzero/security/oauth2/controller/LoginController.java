package top.mxzero.security.oauth2.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Peng
 * @since 2025/4/11
 */
@Controller
public class LoginController {

    @GetMapping("/login")
    public String login(
            @RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "logout", required = false) String logout,
            Model model) {

        if (error != null) {
            model.addAttribute("loginError", "账号或密码错误");
        }

        if (logout != null) {
            model.addAttribute("logoutMessage", "您已成功登出");
        }

        return "login";
    }
}

