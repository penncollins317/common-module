package top.mxzero.security.core.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Peng
 * @email qianmeng6879@163.com
 * @since 2024/8/8
 */
public final class JwtConst {
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final long TOKEN_REFRESH_TIME = 10 * 60 * 1000;
    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private JwtConst() {
    }
}
