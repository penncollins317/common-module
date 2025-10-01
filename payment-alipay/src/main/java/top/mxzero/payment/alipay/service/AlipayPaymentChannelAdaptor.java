package top.mxzero.payment.alipay.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import top.mxzero.payment.PaymentChannelAdaptor;

/**
 * @author Peng
 * @since 2025/10/2
 */
@Slf4j
@Component
public class AlipayPaymentChannelAdaptor implements PaymentChannelAdaptor {
    @Override
    public void close(String outTradeNo) {
        log.info("支付宝关单：{}", outTradeNo);
    }
}