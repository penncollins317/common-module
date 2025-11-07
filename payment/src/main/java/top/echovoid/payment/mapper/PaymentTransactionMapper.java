package top.echovoid.payment.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import top.echovoid.payment.entity.PaymentTransaction;

/**
 * @author Penn Collins
 * @since 2025/10/2
 */
@Mapper
public interface PaymentTransactionMapper extends BaseMapper<PaymentTransaction> {
}
