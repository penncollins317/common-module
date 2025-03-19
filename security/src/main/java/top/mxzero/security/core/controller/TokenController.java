package top.mxzero.security.core.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import top.mxzero.common.dto.RestData;
import top.mxzero.security.core.dto.LoginRequestBody;
import top.mxzero.security.core.dto.RefreshTokenDTO;
import top.mxzero.security.core.dto.TokenDTO;
import top.mxzero.security.core.service.LoginService;

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
