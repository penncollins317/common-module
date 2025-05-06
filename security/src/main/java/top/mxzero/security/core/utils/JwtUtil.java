package top.mxzero.security.core.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import top.mxzero.security.core.enums.TokenType;
import top.mxzero.security.jwt.JwtProps;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * @author Peng
 * @email qianmeng6879@163.com
 * @since 2023/8/15
 */
@Slf4j
@Component
public class JwtUtil implements ApplicationContextAware {
    private static JwtProps JWT_CONFIG_PROPS;


    private static SecretKey generaKey() {
        return Keys.hmacShaKeyFor(JWT_CONFIG_PROPS.getSecret().getBytes(StandardCharsets.UTF_8));
    }

    public static String createToken(String tokenId, String subject, long expireSeconds) {
        Date currentDate = new Date();
        return Jwts.builder().signWith(generaKey(), Jwts.SIG.HS256)
                .subject(subject)
                .issuer(JWT_CONFIG_PROPS.getIssuer())
                .expiration(new Date(currentDate.getTime() + expireSeconds * 1000))
                .id(tokenId)
                .issuedAt(currentDate)
                .compact();
    }

    public static Jws<Claims> parseToken(String token) {
        return Jwts.parser().verifyWith(generaKey()).build().parseSignedClaims(token);
    }

    public static boolean verifyToken(String token) {
        try {
            Jwts.parser().verifyWith(generaKey()).build().parse(token);
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
