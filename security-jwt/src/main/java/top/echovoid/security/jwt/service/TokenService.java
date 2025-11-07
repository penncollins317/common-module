package top.echovoid.security.jwt.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import top.echovoid.security.jwt.dto.TokenDTO;

import java.util.Map;

/**
 * @author Penn Collins
 * @since 2025/5/7
 */
public interface TokenService {

    /**
     * 创建普通token
     *
     * @param tokenId   token唯一标识
     * @param subject   荷载信息
     * @param expireIn  过期时间，秒
     * @param extraData 附加数据
     */
    String createToken(String tokenId, String subject, long expireIn, Map<String, Object> extraData);

    /**
     * 创建包含access token和refresh token
     *
     * @param subject 荷载信息
     */
    TokenDTO createFullToken(String subject);

    /**
     * 验证token是否有效
     *
     * @param token jwt
     */
    boolean verifyToken(String token);

    /**
     * 解析token
     *
     * @param token jwt
     */
    Jws<Claims> parseToken(String token);
}