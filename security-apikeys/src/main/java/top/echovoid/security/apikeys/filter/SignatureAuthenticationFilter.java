package top.echovoid.security.apikeys.filter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import top.echovoid.common.dto.RestData;
import top.echovoid.security.apikeys.components.BodyRepeatReadRequestWrapper;
import top.echovoid.security.apikeys.authencation.SignatureAuthenticationToken;
import top.echovoid.security.apikeys.authencation.OutAppPrincipal;
import top.echovoid.security.apikeys.entity.OutApp;
import top.echovoid.security.apikeys.enums.OutAppStatus;
import top.echovoid.security.apikeys.mapper.OutAppMapper;
import top.echovoid.security.apikeys.utils.SignatureUtil;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;

/**
 * @author Penn Collins
 * @since 2025/1/20
 */
public class SignatureAuthenticationFilter extends OncePerRequestFilter {
    private static final String APPID_KEY = "X-Appid";
    private static final String TIMESTAMP_KEY = "X-Timestamp";
    private static final String NONCE_KEY = "X-Nonce";
    private static final String SIGNATURE_KEY = "X-Signature";
    private static final String NUMBER_PATTERN = "^\\d+$";
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private final AuthenticationDetailsSource<HttpServletRequest, ?> authenticationDetailsSource = new WebAuthenticationDetailsSource();
    private final OutAppMapper outAppMapper;

    public SignatureAuthenticationFilter(OutAppMapper outAppMapper) {
        this.outAppMapper = outAppMapper;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (!request.getMethod().equals("POST") || request.getContentType() == null || !request.getContentType().startsWith("application/json")) {
            this.errorResponse(response, "request error.");
            return;
        }

        if (!this.checkHeaderParams(request)) {
            this.errorResponse(response, "missing request header.");
            return;
        }

        OutAppPrincipal appPrincipal = new OutAppPrincipal(
                Long.valueOf(request.getHeader(APPID_KEY)),
                Long.valueOf(request.getHeader(TIMESTAMP_KEY)),
                request.getHeader(NONCE_KEY),
                request.getHeader(SIGNATURE_KEY)
        );

        BodyRepeatReadRequestWrapper requestWrapper = new BodyRepeatReadRequestWrapper(request);

        Map<String, Object> body = OBJECT_MAPPER.readValue(requestWrapper.getInputStream().readAllBytes(), new TypeReference<>() {
        });


        OutApp app = this.outAppMapper.selectById(Long.valueOf(request.getHeader(APPID_KEY)));

        if (app == null) {
            this.errorResponse(response, "appid error.");
            return;
        }

        if (app.getStatus() != OutAppStatus.ACTIVE) {
            this.errorResponse(response, "app no active.");
            return;
        }

        boolean result = SignatureUtil.validateSignature(Collections.emptyMap(), body, app.getSecretKey(), appPrincipal.getTimestamp(), appPrincipal.getNonce(), appPrincipal.getSignature());
        if (!result) {
            this.errorResponse(response, "signature error.");
            return;
        }

        Object details = authenticationDetailsSource.buildDetails(request);
        SignatureAuthenticationToken authentication = new SignatureAuthenticationToken(appPrincipal, details);
        authentication.setAuthenticated(true);
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);
        filterChain.doFilter(requestWrapper, response);
    }

    public boolean checkHeaderParams(HttpServletRequest request) {
        String appId = request.getHeader(APPID_KEY);
        String timestamp = request.getHeader(TIMESTAMP_KEY);
        String nonce = request.getHeader(NONCE_KEY);
        String signature = request.getHeader(SIGNATURE_KEY);
        if (StringUtils.hasLength(appId) && StringUtils.hasLength(timestamp) &&
                StringUtils.hasLength(nonce) && StringUtils.hasLength(signature)) {
            return appId.matches(NUMBER_PATTERN) && timestamp.matches(NUMBER_PATTERN);
        }
        return false;
    }

    private void errorResponse(HttpServletResponse response, String error) {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        try (PrintWriter writer = response.getWriter()) {
            writer.print(OBJECT_MAPPER.writeValueAsString(RestData.error(error)));
        } catch (IOException ignored) {
        }

    }

}
