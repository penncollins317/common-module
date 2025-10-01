package top.mxzero.payment;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Peng
 * @since 2025/10/2
 */
@Getter
@AllArgsConstructor
public class PaymentAdaptorRegisterBean {
    private final PaymentChannel channel;
    private final PaymentChannelAdaptor adaptor;
}
