package top.echovoid.security.oauth2.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import top.echovoid.security.oauth2.dto.OAuthClientDTO;

@Controller
@RequestMapping("/client")
public class OAuthClientController {
    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        model.addAttribute("oauthClient", new OAuthClientDTO());
        return "client-register";
    }

    @PostMapping("/register")
    public String registerClient(@ModelAttribute("oauthClient") OAuthClientDTO oauthClientDto,
                                 RedirectAttributes redirectAttributes) {
        try {
            redirectAttributes.addFlashAttribute("message", "申请成功，稍后我们会审核您的申请！");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "申请失败：" + e.getMessage());
        }
        return "redirect:/client/register";
    }
}