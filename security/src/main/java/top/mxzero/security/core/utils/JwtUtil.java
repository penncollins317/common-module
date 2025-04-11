package top.mxzero.security.core.utils;

import io.jsonwebtoken.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import top.mxzero.security.core.JwtProps;
import top.mxzero.security.core.enums.TokenType;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.*;

/**
 * @author Peng
 * @email qianmeng6879@163.com
 * @since 2023/8/15
 */
@Slf4j
@Component
public class JwtUtil implements ApplicationContextAware {
    private static JwtProps JWT_CONFIG_PROPS;
    private static final SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS256;

    private static SecretKey generaKey() {
        byte[] encodeKey = Base64.decodeBase64(Base64.encodeBase64(JWT_CONFIG_PROPS.getSecret().getBytes()));
        return new SecretKeySpec(encodeKey, 0, encodeKey.length, "AES");
    }

    public static String createToken(String tokenId, String subject, TokenType tokenType) {
        Date currentDate = new Date();
        Date expireDate;

        if (tokenType == TokenType.ACCESS_TOKEN) {
            expireDate = new Date(currentDate.getTime() + JWT_CONFIG_PROPS.getExpire() * 1000);
        } else {
            expireDate = new Date(currentDate.getTime() + JWT_CONFIG_PROPS.getRefresh() * 1000);
        }
        JwtBuilder jwtBuilder;
        jwtBuilder = Jwts.builder()
                .setId(tokenId)
                .setIssuedAt(currentDate)
                .setIssuer(JWT_CONFIG_PROPS.getIssuer())
                .setSubject(subject)
                .signWith(SIGNATURE_ALGORITHM, generaKey())
                .setExpiration(expireDate);
        return jwtBuilder.compact();
    }

    public static Jws<Claims> parseToken(String token) {
        return Jwts.parser().setSigningKey(generaKey()).parseClaimsJws(token);
    }

    public static boolean verifyToken(String token) {
        try {
            Jwts.parser().setSigningKey(generaKey()).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static String getToken(HttpServletRequest request) {
        String token = request.getParameter(TokenType.ACCESS_TOKEN.getValue());
        if (StringUtils.hasLength(token)) {
            return token;
        }

        String authStr = request.getHeader("Authorization");
        if (StringUtils.hasLength(authStr) && authStr.startsWith("Bearer ")) {
            return authStr.substring(7);
        }

        return null;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        JWT_CONFIG_PROPS = applicationContext.getBean(JwtProps.class);
    }
}
