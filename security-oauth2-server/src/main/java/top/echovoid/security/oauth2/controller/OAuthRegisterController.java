package top.echovoid.security.oauth2.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import top.echovoid.common.exceptions.ServiceException;
import top.echovoid.security.oauth2.dto.RegisterDTO;
import top.echovoid.security.oauth2.service.RegisterService;

/**
 * @author Penn Collins
 * @since 2025/4/12
 */
@AllArgsConstructor
@Controller
public class OAuthRegisterController {
    private final RegisterService registerService;

    @GetMapping("/register")
    public String registerForm() {
        return "register";
    }

    @PostMapping("/register")
    public String doRegister(@Valid RegisterDTO registerDTO, Model model) {
        try {
            this.registerService.registerUser(registerDTO);
        } catch (ServiceException e) {
            model.addAttribute("serviceError", e.getMessage());
            return "/register";
        }
        return "redirect:/register/success";
    }

    @RequestMapping("/register/success")
    public String registerSuccess() {
        return "register-success";
    }
}
