package top.mxzero.security.core.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.mxzero.common.dto.RestData;
import top.mxzero.security.core.dto.LoginRequestBody;
import top.mxzero.security.core.dto.RefreshTokenDTO;
import top.mxzero.security.core.service.LoginService;
import top.mxzero.security.jwt.dto.TokenDTO;

/**
 * 登录接口
 */
@AllArgsConstructor
@RestController
@RequestMapping("/token")
public class TokenController {
    private final LoginService loginService;


    /**
     * 用户名登录
     *
     * @param args 账号信息
     * @return jwt
     */
    @PostMapping("/create")
    public RestData<TokenDTO> createTokenApi(
            @Valid @RequestBody LoginRequestBody args
    ) {
        return RestData.success(this.loginService.loginByUsername(args));
    }

    /**
     * 用户名登录，表单格式
     *
     * @param args 账号信息
     * @return jwt
     */
    @RequestMapping("/create/form")
    public RestData<TokenDTO> createTokenByFormApi(
            @Valid LoginRequestBody args
    ) {
        return RestData.success(this.loginService.loginByUsername(args));
    }

    /**
     * 刷新token接口
     *
     * @param dto refreshToken
     * @return jwt
     */
    @PostMapping("/refresh")
    public RestData<TokenDTO> refreshTokenApi(@Valid @RequestBody RefreshTokenDTO dto) {
        return RestData.success(this.loginService.refresh(dto.getRefresh()));
    }
}
