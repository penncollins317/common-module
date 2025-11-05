package top.mxzero.payment.alipay.service;

import com.alipay.api.AlipayApiException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.mxzero.common.exceptions.ServiceException;
import top.mxzero.payment.dto.PaymentDTO;
import top.mxzero.payment.dto.PaymentTransactionDTO;

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

    public String pagePay(PaymentTransactionDTO transactionDTO) {
        PaymentDTO paymentDTO = transactionDTO.getPaymentDTO();
        try {
            String result = alipayAdaptor.pagePay(transactionDTO.getOutTradeNo(), paymentDTO.getSubject(), paymentDTO.getAmount());
            log.info("alipay call: {} success", transactionDTO.getOutTradeNo());
            return result;
        } catch (AlipayApiException e) {
            log.error(e.getMessage());
            throw new ServiceException("支付宝调用失败");
        }
    }


    public String wayPay(PaymentTransactionDTO transactionDTO) {
        PaymentDTO paymentDTO = transactionDTO.getPaymentDTO();
        try {
            String result = alipayAdaptor.wapPay(transactionDTO.getOutTradeNo(), paymentDTO.getSubject(), paymentDTO.getAmount());
            log.info("alipay call: {} success", transactionDTO.getOutTradeNo());
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
