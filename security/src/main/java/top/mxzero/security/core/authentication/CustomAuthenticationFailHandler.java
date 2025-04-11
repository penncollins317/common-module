package top.mxzero.security.core.authentication;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import top.mxzero.common.dto.RestData;
import top.mxzero.common.utils.JsonUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * @author Peng
 * @since 2025/4/9
 */
public class CustomAuthenticationFailHandler extends SimpleUrlAuthenticationFailureHandler {
    private static final String ERROR_PARAM = "error";

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        if (response.isCommitted()) {
            return;
        }

        // 处理JSON响应
        if ("application/json".equals(request.getHeader("Accept"))) {
            responseJson(response, exception);
            return;
        }

        // 构建重定向URL，保留fallback参数
        String redirectUrl = buildFailureRedirectUrl(request);
        getRedirectStrategy().sendRedirect(request, response, redirectUrl);
    }

    private String buildFailureRedirectUrl(HttpServletRequest request) {
        String fallback = request.getParameter(CustomAuthenticationSuccessHandler.NEXT_URL_PARAM);
        String redirectUrl = "/login?" + ERROR_PARAM;

        if (fallback != null && !fallback.isEmpty()) {
            try {
                // 验证fallback URL是否合法
                URI validatedUri = new URI(fallback);
                redirectUrl += "&" + CustomAuthenticationSuccessHandler.NEXT_URL_PARAM + "=" + URLEncoder.encode(fallback, StandardCharsets.UTF_8);
            } catch (Exception e) {
                logger.error("Invalid fallback parameter: " + fallback);
            }
        }
        return redirectUrl;
    }

    private void responseJson(HttpServletResponse response, AuthenticationException exception) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");

        try (PrintWriter writer = response.getWriter()) {
            writer.print(JsonUtils.stringify(RestData.error("account error.", 401)));
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
}
