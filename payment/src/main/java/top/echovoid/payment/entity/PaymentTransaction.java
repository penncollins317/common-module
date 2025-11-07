package top.echovoid.payment.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
public class PaymentTransaction {

    private Long id;

    /**
     * 关联支付请求ID
     */
    private Long paymentId;

    /**
     * 系统生成的唯一单号
     */
    private String outTradeNo;

    /**
     * 第三方渠道返回的交易号
     */
    private String transactionId;

    /**
     * 支付渠道，例如 alipay, wechat
     */
    private String channel;

    /**
     * 支付金额
     */
    private BigDecimal amount;

    /**
     * 渠道商原始支付状态
     */
    private String channelStatus;

    /**
     * 第三方返回的原始数据，JSON 格式
     */
    private String channelPayload;

    /**
     * 实际支付成功时间
     */
    private LocalDateTime paymentAt;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}
