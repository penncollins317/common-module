package top.mxzero.security.core.controller;

import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Peng
 * @since 2025/4/9
 */
@Controller
@AllArgsConstructor
public class LoginController {
    @RequestMapping("/login")
    public String loginPage(@RequestParam(value = "error", required = false) String error, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (error != null) {
            model.addAttribute("error", "用户名或密码错误");
        }

        return "login";
    }
}
