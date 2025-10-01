package top.mxzero.payment.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import top.mxzero.common.exceptions.ServiceException;
import top.mxzero.payment.PaymentChannelRegistry;
import top.mxzero.payment.entity.PaymentTransaction;
import top.mxzero.payment.mapper.PaymentTransactionMapper;
import top.mxzero.payment.service.PaymentChannelService;

/**
 * @author Peng
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
