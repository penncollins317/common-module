package top.mxzero.payment.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.mxzero.common.exceptions.ServiceErrorCode;
import top.mxzero.common.exceptions.ServiceException;
import top.mxzero.common.utils.DateUtil;
import top.mxzero.common.utils.DeepBeanUtil;
import top.mxzero.payment.PaymentChannel;
import top.mxzero.payment.PaymentChannelAdaptor;
import top.mxzero.payment.PaymentChannelRegistry;
import top.mxzero.payment.dto.*;
import top.mxzero.payment.entity.PaymentGoods;
import top.mxzero.payment.entity.PaymentRequest;
import top.mxzero.payment.entity.PaymentTransaction;
import top.mxzero.payment.enums.PaymentStatus;
import top.mxzero.payment.mapper.PaymentGoodsMapper;
import top.mxzero.payment.mapper.PaymentRequestMapper;
import top.mxzero.payment.mapper.PaymentTransactionMapper;
import top.mxzero.payment.service.PaymentService;

import java.math.BigDecimal;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

/**
 * @author Peng
 * @since 2025/9/30
 */
@Slf4j
@Service
@AllArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private PaymentRequestMapper requestMapper;
    private PaymentTransactionMapper transactionMapper;
    private PaymentGoodsMapper goodsMapper;
    private PaymentChannelRegistry channelRegistry;

    @Override
    @Transactional
    public Long requestPayment(RequestPaymentDTO requestPaymentDTO) {
        PaymentRequest paymentRequest = PaymentRequest.builder()
                .origin(requestPaymentDTO.getOrigin())
                .amount(requestPaymentDTO.getAmount())
                .subject(requestPaymentDTO.getSubject())
                .status(PaymentStatus.PENDING)
                .build();
        this.requestMapper.insert(paymentRequest);
        if (requestPaymentDTO.getGoodsDetail() != null && !requestPaymentDTO.getGoodsDetail().isEmpty()) {
            requestPaymentDTO.getGoodsDetail().forEach(goodsDetail -> {
                PaymentGoods goods = PaymentGoods.builder()
                        .paymentId(paymentRequest.getId())
                        .goodsId(goodsDetail.getGoodsId())
                        .goodsName(goodsDetail.getGoodsName())
                        .quantity(goodsDetail.getQuantity())
                        .price(goodsDetail.getPrice())
                        .build();
                goodsMapper.insert(goods);
            });
        }
        return paymentRequest.getId();
    }

    @Override
    public String createTransaction(Long paymentId, String channel) {
        PaymentRequest paymentRequest = requestMapper.selectById(paymentId);
        if (paymentRequest == null) {
            throw new ServiceException(String.format("支付单 %s 不存在", paymentId));
        }
        if (paymentRequest.getStatus() != PaymentStatus.PENDING) {
            throw new ServiceException("当前支付单无法发起支付");
        }
        return "";
    }

    @Override
    @Transactional
    public PaymentDTO query(Long paymentId) {
        LambdaQueryWrapper<PaymentRequest> queryWrapper = new LambdaQueryWrapper<PaymentRequest>().eq(PaymentRequest::getId, paymentId);
        PaymentRequest paymentRequest = requestMapper.selectOne(queryWrapper);
        if (paymentRequest == null) {
            throw new ServiceException(ServiceErrorCode.RESOURCE_NOT_FOUND);
        }
        PaymentDTO paymentDTO = DeepBeanUtil.copyProperties(paymentRequest, PaymentDTO.class);

        List<PaymentGoods> paymentGoods = this.goodsMapper.selectList(new LambdaQueryWrapper<PaymentGoods>().eq(PaymentGoods::getPaymentId, paymentRequest.getId()));
        paymentDTO.setGoodsDetail(DeepBeanUtil.copyProperties(paymentGoods, GoodsDTO.class));


        // 支付渠道查询
        if (paymentDTO.getStatus() == PaymentStatus.PENDING) {
            LambdaQueryWrapper<PaymentTransaction> paymentTransactionLambdaQueryWrapper = new LambdaQueryWrapper<PaymentTransaction>().eq(PaymentTransaction::getPaymentId, paymentRequest.getId())
                    .orderByDesc(PaymentTransaction::getPaymentId);
            Page<PaymentTransaction> transactionPage = transactionMapper.selectPage(new Page<>(1, 1), paymentTransactionLambdaQueryWrapper);

            if (!transactionPage.getRecords().isEmpty()) {
                PaymentTransaction currentTransaction = transactionPage.getRecords().getFirst();
                PaymentChannelAdaptor channelAdaptor = channelRegistry.getAdaptor(currentTransaction.getChannel());
                PaymentTransactionDTO queryChannelResult = channelAdaptor.query(currentTransaction.getOutTradeNo());
                if (queryChannelResult != null) {
                    if (queryChannelResult.isPaymentSuccess()) {
                        PaymentTransaction editTransaction = PaymentTransaction.builder()
                                .id(currentTransaction.getId())
                                .channelStatus(queryChannelResult.getChannelStatus())
                                .transactionId(queryChannelResult.getTransactionId())
                                .paymentAt(queryChannelResult.getPaymentAt().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime())
                                .build();
                        transactionMapper.updateById(editTransaction);
                        PaymentRequest editRequest = PaymentRequest.builder()
                                .id(paymentId)
                                .paymentAt(editTransaction.getPaymentAt())
                                .status(PaymentStatus.SUCCESS)
                                .build();
                        requestMapper.updateById(editRequest);
                        paymentDTO.setStatus(PaymentStatus.SUCCESS);
                        paymentDTO.setPaymentAt(editRequest.getPaymentAt());
                    }
                }
            }
        }

        return paymentDTO;
    }

    @Override
    public PaymentRefundDTO queryRefund(String outRefundNo) {
        return null;
    }

    @Override
    public String requestRefund(Long paymentId, BigDecimal refundAmount) {
        return "";
    }

    @Override
    public boolean close(String paymentId) {
        return false;
    }

    @Override
    public boolean closeRefund(String outTradeNo, BigDecimal refundAmount) {
        return false;
    }

    @Override
    @Transactional
    public PaymentTransactionDTO createTransaction(Long requestId, PaymentChannel channel) {
        PaymentDTO paymentDTO = this.query(requestId);
        if (paymentDTO == null) {
            throw new ServiceException("支付请求不存在");
        }
        if (paymentDTO.getStatus() != PaymentStatus.PENDING) {
            throw new ServiceException("只有待支付的支付请求才可以创建支付事务");
        }

        String outTradeNo = "P" + DateUtil.formatNumber(new Date()) + IdWorker.getId();
        PaymentTransaction paymentTransaction = PaymentTransaction.builder()
                .paymentId(requestId)
                .amount(paymentDTO.getAmount())
                .outTradeNo(outTradeNo)
                .channel(channel.getCode())
                .build();

        transactionMapper.insert(paymentTransaction);
        return PaymentTransactionDTO.builder()
                .id(paymentTransaction.getId())
                .outTradeNo(outTradeNo)
                .paymentDTO(paymentDTO)
                .channel(channel)
                .amount(paymentDTO.getAmount())
                .remark(paymentDTO.getRemark())
                .build();
    }
}
