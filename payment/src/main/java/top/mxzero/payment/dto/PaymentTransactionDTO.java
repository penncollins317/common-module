package top.mxzero.payment.dto;

import lombok.*;
import top.mxzero.payment.enums.PaymentStatus;

import java.math.BigDecimal;

/**
 * @author Peng
 * @since 2025/10/1
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentTransactionDTO {
    private String outTradeNo;
    private String transactionId;
    private BigDecimal amount;
    private PaymentChannel channel;
    private PaymentStatus status;
    private String remark;
}
