package top.echovoid.payment;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Penn Collins
 * @since 2025/10/2
 */
@Getter
@AllArgsConstructor
public class PaymentAdaptorRegisterBean {
    private final PaymentChannel channel;
    private final PaymentChannelAdaptor adaptor;
}