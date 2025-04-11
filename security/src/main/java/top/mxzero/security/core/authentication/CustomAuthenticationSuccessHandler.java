package top.mxzero.security.core.authentication;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import top.mxzero.common.dto.RestData;
import top.mxzero.common.utils.JsonUtils;
import top.mxzero.security.core.dto.TokenDTO;
import top.mxzero.security.core.service.LoginService;

import java.io.IOException;
import java.io.PrintWriter;

@Slf4j
@Component
@AllArgsConstructor
public class CustomAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final LoginService loginService;
    public static final String NEXT_URL_PARAM = "fallback";

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        if (response.isCommitted()) {
            return;
        }

        if ("application/json".equals(request.getHeader("Accept"))) {
            responseJson(request, response, authentication);
        }

        String targetUrl = determineTargetUrl(request, response, authentication);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    private void responseJson(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        TokenDTO token = loginService.getTokenByUserId(Long.valueOf(authentication.getName()));
        try (PrintWriter writer = response.getWriter()) {
            writer.print(JsonUtils.stringify(RestData.success(token)));
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}