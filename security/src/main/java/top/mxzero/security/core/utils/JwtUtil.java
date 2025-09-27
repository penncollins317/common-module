package top.mxzero.security.core.utils;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.util.StringUtils;

/**
 * @author Peng
 * @email qianmeng6879@163.com
 * @since 2023/8/15
 */
public class JwtUtil {
    private static final String ACCESS_TOKEN_PARAMETER_NAME = "access_token";
    private static final String AUTHORIZATION_HEADER_NAME = "Authorization";
    private static final String AUTHORIZATION_SCHEMA = "Bearer ";

    public static String getToken(HttpServletRequest request) {
        String token = request.getParameter(ACCESS_TOKEN_PARAMETER_NAME);
        if (StringUtils.hasText(token)) {
            return token.trim();
        }

        String authHeader = request.getHeader(AUTHORIZATION_HEADER_NAME);
        if (StringUtils.hasText(authHeader) && authHeader.startsWith(AUTHORIZATION_SCHEMA)) {
            return authHeader.substring(AUTHORIZATION_SCHEMA.length()).trim();
        }
        return null;
    }
}
