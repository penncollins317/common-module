package top.mxzero.security.oauth2.service;

import jakarta.mail.MessagingException;

/**
 * @author Peng
 * @since 2025/4/5
 */
public interface EmailService {
    /**
     * 发送邮箱验证码
     *
     * @param to   目标用户
     * @param code 验证码
     */
    void sendVerificationEmail(String to, String code);

    /**
     * Verify email code
     */
    boolean verifyEmailCode(String email, String code, boolean autoDelete);
}
