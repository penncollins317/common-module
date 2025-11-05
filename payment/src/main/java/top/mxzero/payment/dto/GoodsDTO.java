package top.mxzero.payment.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    @Positive(message = "商品数量必须大于0")
    private int quantity;
    @Positive(message = "商品价格必须大于0")
    private BigDecimal price;
}
