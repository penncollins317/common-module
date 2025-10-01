package top.mxzero.payment.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.mxzero.common.dto.RestData;
import top.mxzero.payment.dto.PaymentDTO;
import top.mxzero.payment.dto.RequestPaymentDTO;
import top.mxzero.payment.service.PaymentService;

/**
 * @author Peng
 * @since 2025/10/1
 */
@AllArgsConstructor
@RestController
public class PaymentTestController {
    private final PaymentService paymentService;


    /**
     * 测试请求接口
     */
    @RequestMapping("/test/payment")
    public RestData<PaymentDTO> requestPaymentTestApi(@Valid RequestPaymentDTO requestPaymentDTO) {
        Long paymentId = paymentService.requestPayment(requestPaymentDTO);
        return RestData.ok(paymentService.query(paymentId));
    }

}
