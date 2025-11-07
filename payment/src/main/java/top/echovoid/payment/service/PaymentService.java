package top.echovoid.payment.service;

import top.echovoid.payment.PaymentChannel;
import top.echovoid.payment.dto.PaymentDTO;
import top.echovoid.payment.dto.PaymentRefundDTO;
import top.echovoid.payment.dto.PaymentTransactionDTO;
import top.echovoid.payment.dto.RequestPaymentDTO;

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
    Long requestPayment(RequestPaymentDTO requestPaymentDTO);

    /**
     * 创建支付渠道单
     */
    String createTransaction(Long paymentId, String channel);

    /**
     * 查询支付信息
     *
     */
    PaymentDTO query(Long paymentId);

    /**
     * 查询退款单
     */
    PaymentRefundDTO queryRefund(String outRefundNo);

    /**
     * 申请退款
     */
    String requestRefund(Long paymentId, BigDecimal refundAmount);

    /**
     * 关闭支付单
     *
     */
    boolean close(String paymentId);

    /**
     * 关闭退款单
     *
     */
    boolean closeRefund(String outTradeNo, BigDecimal refundAmount);

    /**
     * 创建支付事务
     *
     * @param requestId 支付请求ID
     * @param channel   支付渠道
     * @return 支付事务参数
     */
    PaymentTransactionDTO createTransaction(Long requestId, PaymentChannel channel);
}
