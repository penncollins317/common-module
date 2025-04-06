package top.mxzero.security.oauth2.authentication;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import top.mxzero.common.dto.RestData;
import top.mxzero.common.utils.JsonUtils;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author Peng
 * @since 2025/3/19
 */
public class JsonAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        try (PrintWriter writer = response.getWriter()) {
            writer.print(JsonUtils.stringify(RestData.error("FORBIDDEN", 403)));
        }
    }
}
