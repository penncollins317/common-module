package top.echovoid.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.echovoid.payment.PaymentChannel;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author Peng
 * @since 2025/10/1
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentTransactionDTO {
    private Long id;
    private String outTradeNo;
    private String transactionId;
    private BigDecimal amount;
    private PaymentChannel channel;
    private String channelStatus;
    private String remark;
    private Date paymentAt;
    private PaymentDTO paymentDTO;
    private boolean paymentSuccess;
}
