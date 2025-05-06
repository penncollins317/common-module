package top.mxzero.security.core.utils;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.util.StringUtils;
import top.mxzero.security.core.enums.TokenType;

/**
 * @author Peng
 * @email qianmeng6879@163.com
 * @since 2023/8/15
 */
public class JwtUtil {

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
}
