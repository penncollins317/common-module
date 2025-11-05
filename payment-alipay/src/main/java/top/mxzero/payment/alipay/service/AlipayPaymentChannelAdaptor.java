package top.mxzero.payment.alipay.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import top.mxzero.common.utils.DateUtil;
import top.mxzero.payment.PaymentChannel;
import top.mxzero.payment.PaymentChannelAdaptor;
import top.mxzero.payment.alipay.dto.AlipayTradeQueryResponseDTO;
import top.mxzero.payment.dto.PaymentTransactionDTO;

import java.math.BigDecimal;

/**
 * @author Peng
 * @since 2025/10/2
 */
@Slf4j
@Component
@AllArgsConstructor
public class AlipayPaymentChannelAdaptor implements PaymentChannelAdaptor {
    private final AlipayAdaptor alipayAdaptor;

    @Override
    public void close(String outTradeNo) {
        log.info("支付宝关单：{}", outTradeNo);
        try {
            alipayAdaptor.close(outTradeNo, null);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    @Override
    public PaymentTransactionDTO query(String outTradeNo) {
        try {
            AlipayTradeQueryResponseDTO responseDTO = alipayAdaptor.query(outTradeNo, null);
            return PaymentTransactionDTO.builder()
                    .outTradeNo(outTradeNo)
                    .transactionId(responseDTO.getTradeQueryResponse().getTradeNo())
                    .channel(PaymentChannel.ALIPAY)
                    .paymentSuccess(true)
                    .paymentAt(DateUtil.parseDatetime(responseDTO.getTradeQueryResponse().getSendPayDate()))
                    .channelStatus(responseDTO.getTradeQueryResponse().getTradeStatus())
                    .amount(new BigDecimal(responseDTO.getTradeQueryResponse().getTotalAmount()))
                    .build();
        } catch (Exception e) {
            return null;
        }
    }
}