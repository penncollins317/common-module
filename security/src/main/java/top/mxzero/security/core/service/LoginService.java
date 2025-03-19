package top.mxzero.security.core.service;

import top.mxzero.security.core.dto.LoginRequestBody;
import top.mxzero.security.core.dto.TokenDTO;

/**
 * 登录业务
 *
 * @author Peng
 * @since 2025/2/5
 */
public interface LoginService {
    /**
     * 用户登录，获取token
     *
     * @param args 登录账号参数
     * @return jwt
     */
    TokenDTO loginByUsername(LoginRequestBody args);

    /**
     * 通过用户ID获取token
     * @param userId 用户ID
     * @return jwt
     */
    TokenDTO getTokenByUserId(Long userId);

    /**
     * 刷新token
     *
     * @param token refreshToken
     * @return jwt
     */
    TokenDTO refresh(String token);
}