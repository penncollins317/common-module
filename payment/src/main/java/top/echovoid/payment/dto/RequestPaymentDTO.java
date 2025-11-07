package top.echovoid.payment.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author Penn Collins
 * @since 2025/9/30
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestPaymentDTO {
    @NotBlank
    private String origin;
    @NotBlank
    private String subject;
    @NotNull
    @Positive(message = "支付金额必须大于0")
    private BigDecimal amount;
    @Valid
    private List<GoodsDTO> goodsDetail;
}