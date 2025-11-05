package top.mxzero.payment.alipay.coontroller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.mxzero.common.dto.RestData;
import top.mxzero.common.exceptions.ServiceErrorCode;
import top.mxzero.common.exceptions.ServiceException;
import top.mxzero.common.utils.DateUtil;
import top.mxzero.payment.PaymentChannel;
import top.mxzero.payment.alipay.service.AlipayService;
import top.mxzero.payment.dto.PaymentDTO;
import top.mxzero.payment.dto.PaymentTransactionDTO;
import top.mxzero.payment.service.PaymentService;

import java.io.IOException;
import java.math.BigDecimal;
import java.rmi.ServerError;
import java.util.Date;
import java.util.Random;

/**
 * @author Peng
 * @since 2025/10/2
 */
@RestController
@AllArgsConstructor
public class AlipayTestController {
    private final AlipayService alipayService;
    private final PaymentService paymentService;

    @RequestMapping("/test/alipay/barcode")
    public RestData<PaymentDTO> alipayBarcodeApi(@RequestParam String barcode) {
        return RestData.error("err");
    }

    @RequestMapping("/test/alipay/page/{requestId:\\d+}")
    public void alipayPageApi(@PathVariable Long requestId, HttpServletResponse response) throws IOException {
        PaymentDTO paymentDTO = paymentService.query(requestId);
        if(paymentDTO == null){
            throw new ServiceException(ServiceErrorCode.RESOURCE_NOT_FOUND);
        }
        PaymentTransactionDTO transactionDTO = paymentService.createTransaction(requestId, PaymentChannel.ALIPAY);
        String result = alipayService.pagePay(transactionDTO);
        response.sendRedirect(result);
    }
}
