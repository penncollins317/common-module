package top.mxzero.payment.enums;

/**
 * @author Peng
 * @since 2025/9/30
 */
public enum PaymentStatus {
    PENDING,      // 支付请求已创建，等待用户操作或接口返回
    PROCESSING,   // 用户支付中（扫码/付款码/等待用户输入密码）
    SUCCESS,      // 支付完成
    FAILED,       // 支付失败（接口调用失败、支付拒绝等）
    CLOSED,       // 支付关闭/取消/过期
    REFUNDED      // 支付完成后退款
}