package top.mxzero.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.mxzero.payment.enums.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Peng
 * @since 2025/9/30
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDTO {
    private String subject;
    private String outTradeNo;
    private BigDecimal amount;
    private PaymentStatus status;
    private LocalDateTime paymentAt;
    private List<GoodsDTO> goodsDetail;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}