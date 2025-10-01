package top.mxzero.payment.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author Peng
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
    private BigDecimal amount;
    @Valid
    private List<GoodsDTO> goodsDetail;
}