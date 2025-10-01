package top.mxzero.payment.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import top.mxzero.payment.entity.PaymentTransaction;

/**
 * @author Peng
 * @since 2025/10/2
 */
@Mapper
public interface PaymentTransactionMapper extends BaseMapper<PaymentTransaction> {
}
