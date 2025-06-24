package top.mxzero.security.core.authentication;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import top.mxzero.common.dto.RestData;
import top.mxzero.common.utils.JsonUtils;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author Peng
 * @since 2025/4/9
 */
public class CustomAuthenticationFailHandler extends SimpleUrlAuthenticationFailureHandler {
    public CustomAuthenticationFailHandler() {
        super();
    }

    public CustomAuthenticationFailHandler(String defaultFailureUrl) {
        super(defaultFailureUrl);
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.startsWith(MediaType.APPLICATION_JSON_VALUE)) {
            responseJson(request, response, exception);
        } else {
            super.onAuthenticationFailure(request, response, exception);
        }
    }

    private void responseJson(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        try (PrintWriter writer = response.getWriter()) {
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            writer.print(JsonUtils.stringify(RestData.error("Bad credentials")));
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
}
