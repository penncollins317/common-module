package top.echovoid.payment.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import top.echovoid.common.dto.RestData;
import top.echovoid.payment.dto.PaymentDTO;
import top.echovoid.payment.dto.RequestPaymentDTO;
import top.echovoid.payment.service.PaymentService;

/**
 * 公共支付接口
 *
 * @author Penn Collins
 * @since 2025/10/1
 */
@AllArgsConstructor
@RestController
public class PaymentController {
    private final PaymentService paymentService;


    /**
     * 测试请求接口
     */
    @PostMapping("/payment/test/")
    public RestData<PaymentDTO> requestPaymentTestApi(@RequestBody @Valid RequestPaymentDTO requestPaymentDTO) {
        Long paymentId = paymentService.requestPayment(requestPaymentDTO);
        return RestData.ok(paymentService.query(paymentId));
    }

    /**
     * 查询支付结果
     *
     * @param requestId 支付请求ID
     */
    @RequestMapping("/payment/query/{requestId:\\d+}")
    public RestData<PaymentDTO> queryPaymentApi(@PathVariable Long requestId) {
        return RestData.ok(paymentService.query(requestId));
    }
}
