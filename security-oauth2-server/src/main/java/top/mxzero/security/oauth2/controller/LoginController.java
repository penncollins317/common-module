package top.mxzero.security.oauth2.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;

/**
 * @author Peng
 * @since 2025/4/11
 */
@Controller
public class LoginController {

    @GetMapping("/login")
    public String login(Model model) {

        return "login";
    }
}

