package top.mxzero.payment.dto;

import lombok.*;

import java.math.BigDecimal;

/**
 * @author Peng
 * @since 2025/10/1
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRefundDTO {
    private String outTradeNo;
    private String outRefundNo;
    private BigDecimal refundAmount;
}
