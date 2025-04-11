package top.mxzero.security.core.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import top.mxzero.common.dto.RestData;
import top.mxzero.service.user.dto.UsernamePasswordArgs;
import top.mxzero.service.user.service.UserService;

/**
 * 注册接口
 *
 * @author Peng
 * @since 2025/1/21
 */
@Slf4j
@Controller
@AllArgsConstructor
public class RegisterController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @RequestMapping("/register")
    public ModelAndView registerPage(@RequestParam(value = "error", required = false) String error) {
        ModelAndView mav = new ModelAndView("register");
        if (StringUtils.hasLength(error)) {
            mav.addObject("error", error);
        }
        return mav;
    }

    @PostMapping("/register.action")
    public String registerAction(@Valid UsernamePasswordArgs args) {
        try {
            args.setPassword(passwordEncoder.encode(args.getPassword()));
            Long userId = this.userService.addUser(args);
            return "redirect:/login?success=" + userId;
        } catch (Exception e) {

            return "redirect:/register?error=" + e.getMessage();
        }
    }

    /**
     * 用户注册
     */
    @ResponseBody
    @PostMapping("/public/register")
    public RestData<String> registerApi(@Valid @RequestBody UsernamePasswordArgs args) {
        args.setPassword(passwordEncoder.encode(args.getPassword()));
        return RestData.success(this.userService.addUser(args).toString());
    }
}
