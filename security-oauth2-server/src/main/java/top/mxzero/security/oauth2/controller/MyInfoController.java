package top.mxzero.security.oauth2.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import top.mxzero.service.user.dto.UserinfoDTO;
import top.mxzero.service.user.service.UserService;

import java.security.Principal;

/**
 * @author Peng
 * @since 2025/3/31
 */
@Controller
@AllArgsConstructor
public class MyInfoController {
    private UserService userService;

    @RequestMapping("/")
    public String indexPage(Principal principal, Model model) {
        UserinfoDTO userinfo = this.userService.getUserinfo(Long.valueOf(principal.getName()));
        model.addAttribute("user", userinfo);
        return "redirect:/my";
    }

    @RequestMapping("/my")
    public String myInfoPage(Principal principal, Model model) {
        UserinfoDTO userinfo = this.userService.getUserinfo(Long.valueOf(principal.getName()));
        model.addAttribute("user", userinfo);
        return "my";
    }
}
