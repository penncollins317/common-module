package top.echovoid.payment.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import top.echovoid.common.exceptions.ServiceException;
import top.echovoid.payment.PaymentChannelRegistry;
import top.echovoid.payment.entity.PaymentTransaction;
import top.echovoid.payment.mapper.PaymentTransactionMapper;
import top.echovoid.payment.service.PaymentChannelService;

/**
 * @author Penn Collins
 * @since 2025/10/2
 */
@Service
@AllArgsConstructor
public class PaymentChannelServiceImpl implements PaymentChannelService {
    private final PaymentChannelRegistry channelRegistry;
    private final PaymentTransactionMapper transactionMapper;

    @Override
    public void close(String outTradeNo) {
        PaymentTransaction transaction = transactionMapper.selectOne(new LambdaQueryWrapper<PaymentTransaction>().eq(PaymentTransaction::getOutTradeNo, outTradeNo));
        if (transaction == null) {
            throw new ServiceException("支付事务单不存在");
        }
        channelRegistry.getAdaptor(transaction.getChannel()).close(outTradeNo);
    }
}
