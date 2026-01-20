package top.echovoid.security.core.authentication;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import top.echovoid.common.dto.RestData;
import top.echovoid.common.utils.JsonUtils;
import top.echovoid.security.core.dto.TokenDTO;
import top.echovoid.security.core.service.LoginService;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

@Slf4j
@Component
@AllArgsConstructor
public class CustomAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private final LoginService loginService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.startsWith(MediaType.APPLICATION_JSON_VALUE)) {
            responseJson(request, response, authentication);
            super.clearAuthenticationAttributes(request);
        } else {
            super.onAuthenticationSuccess(request, response, authentication);
        }
    }

    private void responseJson(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        TokenDTO token = loginService.getTokenByUserId(Long.valueOf(authentication.getName()));
        try (PrintWriter writer = response.getWriter()) {
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            writer.print(JsonUtils.stringify(RestData.success(token)));
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}