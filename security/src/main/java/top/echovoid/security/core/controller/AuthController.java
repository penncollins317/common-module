package top.echovoid.security.core.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.echovoid.common.dto.RestData;
import top.echovoid.common.exceptions.ServiceException;
import top.echovoid.security.core.dto.LoginRequestBody;
import top.echovoid.security.core.service.LoginService;
import top.echovoid.security.core.dto.TokenDTO;

/**
 * @author Penn Collins
 * @email penncollins317@gmail.com
 * @since 2026/1/20
 */
@AllArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {
    private final LoginService loginService;

    @PostMapping("/login")
    public RestData<TokenDTO> loginApi(
            @Valid LoginRequestBody loginRequestBody
    ) {
        try {
            TokenDTO tokenDTO = loginService.loginByUsername(loginRequestBody);
            return RestData.ok(tokenDTO);
        } catch (AuthenticationException e) {
            throw new ServiceException(e.getMessage());
        }
    }
}



