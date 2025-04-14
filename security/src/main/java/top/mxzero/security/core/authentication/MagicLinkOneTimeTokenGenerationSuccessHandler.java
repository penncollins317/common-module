package top.mxzero.security.core.authentication;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.http.MediaType;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.authentication.ott.OneTimeToken;
import org.springframework.security.web.authentication.ott.OneTimeTokenGenerationSuccessHandler;
import org.springframework.security.web.authentication.ott.RedirectOneTimeTokenGenerationSuccessHandler;
import org.springframework.security.web.util.UrlUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import top.mxzero.common.dto.RestData;
import top.mxzero.common.utils.JsonUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

@Component
@AllArgsConstructor
public class MagicLinkOneTimeTokenGenerationSuccessHandler implements OneTimeTokenGenerationSuccessHandler {
    private final MailProperties mailProperties;
    private final MailSender mailSender;

    private final OneTimeTokenGenerationSuccessHandler redirectHandler = new RedirectOneTimeTokenGenerationSuccessHandler("/ott/sent");


    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, OneTimeToken oneTimeToken) throws IOException, ServletException {
        String magicLink = UriComponentsBuilder.fromUriString(UrlUtils.buildFullRequestUrl(request))
                .replacePath(request.getContextPath())
                .path("/login/ott")
                .queryParam("token", oneTimeToken.getTokenValue()).toUriString();
        String email = getUserEmail(oneTimeToken.getUsername());
        SimpleMailMessage message = new SimpleMailMessage();
        String companyName = mailProperties.getProperties().get("company.name");
        message.setFrom(String.format("%s<%s>", companyName, mailProperties.getUsername()));
        message.setSubject("Your Spring Security One Time Token");
        message.setTo(email);
        message.setText(String.format("Use the following link to sign in into the application:%s", magicLink));
        this.mailSender.send(message);
        String accept = request.getHeader("Accept");

        if (accept != null && accept.startsWith(MediaType.APPLICATION_JSON_VALUE)) {
            try (PrintWriter printWriter = response.getWriter()) {
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                response.setCharacterEncoding(StandardCharsets.UTF_8.name());
                printWriter.print(JsonUtils.stringify(RestData.success("发送验证码成功")));
            } catch (IOException ignored) {
            }
        } else {
            this.redirectHandler.handle(request, response, oneTimeToken);
        }
    }

    private String getUserEmail(String username) {
        return "qianmeng6879@163.com";
    }

}