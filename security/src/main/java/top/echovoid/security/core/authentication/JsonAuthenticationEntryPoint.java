package top.echovoid.security.core.authentication;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import top.echovoid.common.dto.RestData;
import top.echovoid.common.utils.JsonUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

/**
 * @author Penn Collins
 * @since 2025/3/19
 */
@AllArgsConstructor
public class JsonAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private final LoginUrlAuthenticationEntryPoint defaultEntryPoint;
    private final boolean forceJson;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        String accept = request.getHeader("Accept");
        if (forceJson || (accept != null && accept.startsWith(MediaType.APPLICATION_JSON_VALUE))) {
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            try (PrintWriter writer = response.getWriter()) {
                writer.print(JsonUtils.stringify(RestData.error("UNAUTHORIZED", HttpServletResponse.SC_UNAUTHORIZED)));
            }
        } else {
            this.defaultEntryPoint.commence(request, response, authException);
        }
    }
}
