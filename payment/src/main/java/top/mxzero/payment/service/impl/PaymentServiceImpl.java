package top.mxzero.payment.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.mxzero.common.exceptions.ServiceException;
import top.mxzero.common.utils.DeepBeanUtil;
import top.mxzero.payment.dto.GoodsDTO;
import top.mxzero.payment.dto.PaymentDTO;
import top.mxzero.payment.dto.PaymentRefundDTO;
import top.mxzero.payment.dto.RequestPaymentDTO;
import top.mxzero.payment.entity.PaymentGoods;
import top.mxzero.payment.entity.PaymentRequest;
import top.mxzero.payment.enums.PaymentStatus;
import top.mxzero.payment.mapper.PaymentGoodsMapper;
import top.mxzero.payment.mapper.PaymentRequestMapper;
import top.mxzero.payment.service.PaymentService;

import java.math.BigDecimal;
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
    private PaymentGoodsMapper goodsMapper;

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
    public PaymentDTO query(Long paymentId) {
        LambdaQueryWrapper<PaymentRequest> queryWrapper = new LambdaQueryWrapper<PaymentRequest>().eq(PaymentRequest::getId, paymentId);
        PaymentRequest paymentRequest = requestMapper.selectOne(queryWrapper);
        if (paymentRequest == null) {
            return null;
        }
        PaymentDTO paymentDTO = DeepBeanUtil.copyProperties(paymentRequest, PaymentDTO.class);

        List<PaymentGoods> paymentGoods = this.goodsMapper.selectList(new LambdaQueryWrapper<PaymentGoods>().eq(PaymentGoods::getPaymentId, paymentRequest.getId()));
        paymentDTO.setGoodsDetail(DeepBeanUtil.copyProperties(paymentGoods, GoodsDTO.class));

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
}
