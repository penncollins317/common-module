package top.mxzero.payment;

import top.mxzero.payment.dto.PaymentTransactionDTO;

/**
 * @author Peng
 * @since 2025/10/2
 */
public interface PaymentChannelAdaptor {
    /**
     * 关闭支付单
     *
     * @param outTradeNo 交易号
     */
    void close(String outTradeNo);

    /**
     * 查询支付记录
     *
     * @param outTradeNo 交易号
     */
    PaymentTransactionDTO query(String outTradeNo);
}