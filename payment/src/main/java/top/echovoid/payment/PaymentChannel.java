package top.echovoid.payment;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Peng
 * @since 2025/10/1
 */
@Getter
@AllArgsConstructor
public final class PaymentChannel {
    public static final PaymentChannel ALIPAY = new PaymentChannel("alipay");
    public static final PaymentChannel WECHAT_PAY = new PaymentChannel("wechat_pay");
    private final String code;

    public static PaymentChannel of(String code) {
        return new PaymentChannel(code);
    }
}
