package top.echovoid.payment.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.echovoid.payment.enums.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author Penn Collins
 * @since 2025/10/1
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequest {
    private Long id;
    private String subject;
    private PaymentStatus status;
    private BigDecimal amount;
    private LocalDateTime paymentAt;
    private String origin;
    private String remark;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}
