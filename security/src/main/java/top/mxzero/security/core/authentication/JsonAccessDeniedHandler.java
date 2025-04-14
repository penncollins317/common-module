package top.mxzero.security.core.authentication;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import top.mxzero.common.dto.RestData;
import top.mxzero.common.utils.JsonUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

/**
 * @author Peng
 * @since 2025/3/19
 */
@AllArgsConstructor
public class JsonAccessDeniedHandler implements AccessDeniedHandler {
    private final AccessDeniedHandler defaultAccessDeniedHandler;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {

        String accept = request.getHeader("Accept");
        if (accept != null && accept.startsWith(MediaType.APPLICATION_JSON_VALUE)) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            try (PrintWriter writer = response.getWriter()) {
                writer.print(JsonUtils.stringify(RestData.error("FORBIDDEN", (HttpServletResponse.SC_FORBIDDEN))));
            }
        } else {
            this.defaultAccessDeniedHandler.handle(request, response, accessDeniedException);
        }
    }
}
