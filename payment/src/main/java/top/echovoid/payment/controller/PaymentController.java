package top.echovoid.payment.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "公共支付接口", description = "提供支付请求发起、支付结果查询等通用支付功能")
@AllArgsConstructor
@RestController
public class PaymentController {
    private final PaymentService paymentService;

    /**
     * 测试请求接口
     */
    @Operation(summary = "发起支付测试", description = "模拟发起一笔支付请求并立刻查询其状态")
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
    @Operation(summary = "查询支付结果", description = "根据支付请求ID查询支付的详细状态")
    @GetMapping("/payment/query/{requestId:\\d+}")
    public RestData<PaymentDTO> queryPaymentApi(@Parameter(description = "支付请求唯一标识ID") @PathVariable Long requestId) {
        return RestData.ok(paymentService.query(requestId));
    }
}
