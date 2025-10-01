package top.mxzero.payment.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;
    private String subject;
    private String outTradeNo;
    private BigDecimal amount;
    private PaymentStatus status;
    private List<GoodsDTO> goodsDetail;
    private LocalDateTime paymentAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}