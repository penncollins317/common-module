package top.echovoid.payment.alipay.coontroller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.echovoid.common.dto.RestData;
import top.echovoid.common.exceptions.ServiceErrorCode;
import top.echovoid.common.exceptions.ServiceException;
import top.echovoid.payment.PaymentChannel;
import top.echovoid.payment.alipay.service.AlipayService;
import top.echovoid.payment.dto.PaymentDTO;
import top.echovoid.payment.dto.PaymentTransactionDTO;
import top.echovoid.payment.service.PaymentService;

import java.io.IOException;

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
