package top.mxzero.payment.alipay.service;

import com.alipay.api.AlipayApiException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.mxzero.common.exceptions.ServiceException;
import top.mxzero.payment.dto.PaymentDTO;

import java.util.Map;

/**
 * @author Peng
 * @since 2025/10/1
 */
@Slf4j
@Service
@AllArgsConstructor
public class AlipayService {
    private final AlipayAdaptor alipayAdaptor;

    public String pagePay(PaymentDTO payRequest) {
        try {
            String result = alipayAdaptor.pagePay(payRequest.getOutTradeNo(), payRequest.getSubject(), payRequest.getAmount());
            log.info("alipay call: {} success", payRequest.getOutTradeNo());
            return result;
        } catch (AlipayApiException e) {
            log.error(e.getMessage());
            throw new ServiceException("支付宝调用失败");
        }
    }


    public String wayPay(PaymentDTO payRequest) {
        try {
            String result = alipayAdaptor.wapPay(payRequest.getOutTradeNo(), payRequest.getSubject(), payRequest.getAmount());
            log.info("alipay call: {} success", payRequest.getOutTradeNo());
            return result;
        } catch (AlipayApiException e) {
            log.error(e.getMessage());
            throw new ServiceException("支付宝调用失败");
        }
    }


    public boolean verify(Map<String, String> notifyData) {
        String signType = notifyData.remove("sign_type");
        notifyData.remove("sign");
        if (!alipayAdaptor.verify(notifyData, signType)) {
            return false;
        }
        // 继续后续任务
        try {
            alipayAdaptor.query(notifyData.get("out_trade_no"), null);
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
        return true;
    }
}
