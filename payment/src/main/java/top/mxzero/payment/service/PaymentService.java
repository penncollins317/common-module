package top.mxzero.payment.service;

import top.mxzero.payment.dto.PaymentDTO;
import top.mxzero.payment.dto.PaymentRefundDTO;
import top.mxzero.payment.dto.RequestPaymentDTO;

import java.math.BigDecimal;

/**
 * @author Peng
 * @since 2025/9/30
 */
public interface PaymentService {
    /**
     *
     * 请求支付
     *
     */
    PaymentDTO requestPayment(RequestPaymentDTO requestPaymentDTO);

    /**
     * 查询支付信息
     *
     */
    PaymentDTO query(String outTradeNo);

    /**
     * 查询退款单
     */
    PaymentRefundDTO queryRefund(String outRefundNo);

    /**
     * 申请退款
     */
    String requestRefund(String outTradeNo, BigDecimal refundAmount);

    /**
     * 取消支付单
     *
     */
    boolean cancel(String outTradeNo);

    /**
     * 取消退款单
     *
     */
    boolean cancelRefund(String outTradeNo, BigDecimal refundAmount);
}
