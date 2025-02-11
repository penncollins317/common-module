package top.mxzero.security.core.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.web.bind.annotation.*;
import top.mxzero.security.core.dto.LoginRequestBody;
import top.mxzero.security.core.service.LoginService;

import java.util.Map;

/**
 * 登录接口
 */
@AllArgsConstructor
@RestController
@RequestMapping("/token")
public class TokenController {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private final JwtEncoder jwtEncoder;
    private final JwtDecoder jwtDecoder;
    private final LoginService loginService;


    /**
     * 用户名登录
     *
     * @param args 账号信息
     * @return jwt
     */
    @PostMapping("/create")
    public Map<String, Object> createTokenApi(
            @Valid @RequestBody LoginRequestBody args,
            HttpServletResponse response
    ) {
        try {
            return OBJECT_MAPPER.convertValue(this.loginService.loginByUsername(args), new TypeReference<Map<String, Object>>() {
            });
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return Map.of("error", e.getMessage());
        }
    }


    /**
     * 刷新token接口
     *
     * @param token refreshToken
     * @return jwt
     */
    @RequestMapping("/refresh")
    public Map<String, Object> refreshTokenApi(@RequestParam("token") String token, HttpServletResponse response) {
        try {
            return OBJECT_MAPPER.convertValue(this.loginService.refresh(token), new TypeReference<Map<String, Object>>() {
            });
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return Map.of("error", e.getMessage());
        }
    }
}
