package top.mxzero.security.core.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
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

    @RequestMapping("/my")
    public String indexPage(Principal principal, Model model) {
        if (principal instanceof OAuth2AuthenticationToken oAuth2User) {
            model.addAttribute("attrs", oAuth2User.getPrincipal().getAttributes());
        }
        return "my";
    }
}
