package top.echovoid.security.core.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.echovoid.common.dto.RestData;
import top.echovoid.security.core.dto.LoginRequestBody;
import top.echovoid.security.core.dto.RefreshTokenDTO;
import top.echovoid.security.core.service.LoginService;
import top.echovoid.security.core.dto.TokenDTO;

/**
 * 登录接口
 */
@Tag(name = "令牌接口", description = "提供基于用户名密码或刷新令牌获取访问令牌的功能")
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
    @Operation(summary = "创建令牌 (JSON)", description = "通过提交 JSON 格式的用户名和密码来获取访问令牌")
    @PostMapping("/create")
    public RestData<TokenDTO> createTokenApi(
            @Valid @RequestBody LoginRequestBody args) {
        return RestData.success(this.loginService.loginByUsername(args));
    }

    /**
     * 用户名登录，表单格式
     *
     * @param args 账号信息
     * @return jwt
     */
    @Operation(summary = "创建令牌 (表单)", description = "通过提交表单格式的用户名和密码来获取访问令牌")
    @PostMapping("/create/form")
    public RestData<TokenDTO> createTokenByFormApi(
            @Valid LoginRequestBody args) {
        return RestData.success(this.loginService.loginByUsername(args));
    }

    /**
     * 刷新token接口
     *
     * @param dto refreshToken
     * @return jwt
     */
    @Operation(summary = "刷新令牌", description = "使用有效的刷新令牌来获取新的访问令牌")
    @PostMapping("/refresh")
    public RestData<TokenDTO> refreshTokenApi(@Valid @RequestBody RefreshTokenDTO dto) {
        return RestData.success(this.loginService.refresh(dto.getRefresh()));
    }
}
