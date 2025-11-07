package top.echovoid.payment.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.echovoid.payment.enums.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Penn Collins
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
    private BigDecimal amount;
    private String remark;
    private PaymentStatus status;
    private List<GoodsDTO> goodsDetail;
    private LocalDateTime paymentAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}