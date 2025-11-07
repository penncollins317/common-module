package top.echovoid.security.oauth2.service.impl;

import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import top.echovoid.security.oauth2.service.EmailService;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class EmailServiceImpl implements EmailService {
    private static final long MAIL_LIMIT_MINUTES = 12;
    private static final String KEY_FORMAT = "mail:code:%s";
    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;
    private final StringRedisTemplate redisTemplate;
    @Value("${spring.mail.username}")
    private String emailUsername;
    @Value("${spring.mail.properties.company.name}")
    private String companyName;

    public EmailServiceImpl(JavaMailSender mailSender, StringRedisTemplate redisTemplate, SpringTemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
        this.redisTemplate = redisTemplate;
    }

    /**
     * 发送验证码邮件
     *
     * @param to   收件人邮箱
     * @param code 6位验证码
     */
    @Async
    public void sendVerificationEmail(String to, String code) {
        try {
            String key = String.format(KEY_FORMAT, to);
            Long expire = this.redisTemplate.getExpire(key, TimeUnit.MINUTES);
            if (expire != null && expire >= MAIL_LIMIT_MINUTES) {
                log.debug("mail {} code limit.", to);
                return;
            }
            this.redisTemplate.opsForValue().set(key, code, 15, TimeUnit.MINUTES);
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(to);
            helper.setSubject("您的验证码");
            helper.setFrom(String.format("Your Company<%s>", this.emailUsername));
            Context context = new Context();
            context.setVariable("code", code);
            context.setVariable("companyName", this.companyName);
            context.setVariable("expire", 15);
            String htmlContent = templateEngine.process("verification-email", context);

            helper.setText(htmlContent, true);

            mailSender.send(message);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    @Override
    public boolean verifyEmailCode(String email, String code, boolean autoDelete) {
        String key = String.format(KEY_FORMAT, email);
        String storedCode = autoDelete
                ? this.redisTemplate.opsForValue().getAndDelete(key)
                : this.redisTemplate.opsForValue().get(key);
        return code.equals(storedCode);
    }
}
