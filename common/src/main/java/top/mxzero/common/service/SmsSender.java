package top.mxzero.common.service;

/**
 * @author Peng
 * @since 2024/10/5
 */
public interface SmsSender {
    /**
     * 发送短信验证验证码
     * @param phone 手机号码
     * @param code 验证码
     */
    public void sendSmsAction(String phone, String code);
}
