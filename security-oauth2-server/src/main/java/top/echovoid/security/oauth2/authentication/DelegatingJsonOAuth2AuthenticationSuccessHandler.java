package top.echovoid.security.oauth2.authentication;

import io.netty.util.CharsetUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import top.echovoid.common.dto.RestData;
import top.echovoid.common.utils.JsonUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author Peng
 * @since 2025/4/12
 */
@Slf4j
@AllArgsConstructor
public class DelegatingJsonOAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private final AuthenticationSuccessHandler defaultHandler;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String accept = request.getHeader("Accept");
        this.jsonResponseHandle(request, response, authentication);
    }

    private void jsonResponseHandle(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        try (PrintWriter writer = response.getWriter()) {
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding(CharsetUtil.UTF_8.name());
            Map<String, String> result = new HashMap<>();
            result.put("code", UUID.randomUUID().toString());
            result.put("state", request.getParameter("state"));
            result.put("client_id", request.getParameter("client_id"));
            result.put("redirect_uri", request.getParameter("redirect_uri"));
            writer.print(JsonUtils.stringify(RestData.success(result)));
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
