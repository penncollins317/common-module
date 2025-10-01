package top.mxzero.payment.alipay.coontroller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.mxzero.common.dto.RestData;
import top.mxzero.payment.alipay.service.AlipayService;
import top.mxzero.payment.dto.PaymentDTO;
import top.mxzero.payment.service.PaymentService;

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
    public RestData<PaymentDTO> alipayBarcodeApi(){


    }
}
