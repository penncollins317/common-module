package top.echovoid.payment.service;

/**
 * @author Penn Collins
 * @since 2025/10/2
 */
public interface PaymentChannelService {
    void close(String outTradeNo);
}