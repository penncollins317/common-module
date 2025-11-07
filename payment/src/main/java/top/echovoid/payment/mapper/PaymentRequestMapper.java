package top.echovoid.payment.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import top.echovoid.payment.entity.PaymentRequest;

/**
 * @author Peng
 * @since 2025/10/1
 */
@Mapper
public interface PaymentRequestMapper extends BaseMapper<PaymentRequest> {
}
