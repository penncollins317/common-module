package top.mxzero.payment;

/**
 * @author Peng
 * @since 2025/10/2
 */
public interface PaymentChannelAdaptor {
    void close(String outTradeNo);
}
