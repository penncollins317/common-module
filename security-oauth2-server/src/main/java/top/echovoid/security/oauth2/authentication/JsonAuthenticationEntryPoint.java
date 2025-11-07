package top.echovoid.security.oauth2.authentication;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import top.echovoid.common.dto.RestData;
import top.echovoid.common.utils.JsonUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

/**
 * @author Penn Collins
 * @since 2025/3/19
 */
public class JsonAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        try (PrintWriter writer = response.getWriter()) {
            writer.print(JsonUtils.stringify(RestData.error("UNAUTHORIZED", 401)));
        }
    }
}
