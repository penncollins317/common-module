package top.mxzero.security.core.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import top.mxzero.security.core.annotations.AuthenticatedRequired;
import top.mxzero.security.core.service.LoginService;

import java.security.Principal;

/**
 * @author Peng
 * @since 2025/4/9
 */
@Controller
@AllArgsConstructor
public class LoginController {
    private final LoginService loginService;

    @RequestMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error, @RequestParam(value = "fallback", required = false) String fallback, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {

        }
        if (error != null) {
            model.addAttribute("error", "用户名或密码错误");
        }

        if (fallback != null) {
            model.addAttribute("fallback", fallback);
        }

        return "login";
    }

    @AuthenticatedRequired
    @RequestMapping("/login/success")
    public void loginSuccessHandlerEndpoint(String fallback, Principal principal, HttpServletResponse response) {

    }
}
