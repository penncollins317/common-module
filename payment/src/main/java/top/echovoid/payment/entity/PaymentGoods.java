package top.echovoid.payment.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author Penn Collins
 * @since 2025/10/1
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentGoods {
    private Long id;
    private Long paymentId;
    private String goodsId;
    private String goodsName;
    private BigDecimal price;
    private Integer quantity;
}
