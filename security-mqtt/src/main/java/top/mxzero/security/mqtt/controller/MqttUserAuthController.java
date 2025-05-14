package top.mxzero.security.mqtt.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import top.mxzero.common.dto.RestData;
import top.mxzero.security.core.dto.LoginRequestBody;
import top.mxzero.security.jwt.JwtProps;
import top.mxzero.security.jwt.dto.TokenDTO;
import top.mxzero.security.jwt.service.TokenService;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

/**
 * MQTT 认证接口
 *
 * @author Peng
 * @since 2025/5/14
 */
@Slf4j
@RestController
@AllArgsConstructor
public class MqttUserAuthController {
    private final UserDetailsService userDetailsService;
    private final TokenService tokenService;
    private final JwtProps jwtProps;

    /**
     * 获取token
     */
    @PostMapping("/auth/mqtt")
    public RestData<TokenDTO> mqttUserAuthApi(@Valid @RequestBody LoginRequestBody args) {
        try {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(args.getUsername());
            String token = tokenService.createToken(UUID.randomUUID().toString(), userDetails.getUsername(), jwtProps.getExpire(), Map.of("type", "device_client"));
            TokenDTO tokenDTO = TokenDTO.builder().accessToken(token).expire(jwtProps.getExpire()).expireTime(new Date(System.currentTimeMillis() + jwtProps.getExpire() * 1000)).build();
            return RestData.ok(tokenDTO);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return RestData.error("认证失败");
    }
}
