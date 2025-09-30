package top.mxzero.payment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;

/**
 * @author Peng
 * @since 2025/9/30
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GoodsDTO {
    @NotBlank
    private String goodsId;
    @NotBlank
    private String goodsName;
    @NotNull
    @Size(min = 1)
    private Integer quantity;
    @NotNull
    private BigDecimal price;
}
