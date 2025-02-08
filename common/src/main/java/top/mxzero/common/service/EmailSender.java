package top.mxzero.common.service;

import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;

/**
 * @author Peng
 * @email qianmeng6879@163.com
 * @since 2024/1/22
 */
@Slf4j
public class EmailSender {
    @Autowired(required = false)
    private JavaMailSender javaMailSender;

    @Autowired(required = false)
    private MailProperties mailProperties;

    @Async
    public void send(String content, String toUser) {
        if (this.javaMailSender == null) {
            log.error("Mail Not Configure");
            return;
        }
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            message.setFrom("应用助手<" + mailProperties.getUsername() + ">");
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(toUser);
            helper.setText(content, true);
            helper.setSubject("验证码");
            javaMailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
