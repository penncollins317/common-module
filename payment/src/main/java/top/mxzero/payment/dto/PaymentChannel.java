package top.mxzero.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Peng
 * @since 2025/10/1
 */
@Getter
@AllArgsConstructor
public class PaymentChannel {
    public static final PaymentChannel ALIPAY = new PaymentChannel("alipay");
    public static final PaymentChannel WECHAT_PAY = new PaymentChannel("wechat_pay");
    private final String code;
}
