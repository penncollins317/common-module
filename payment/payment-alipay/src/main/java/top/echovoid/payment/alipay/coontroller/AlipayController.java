package top.echovoid.payment.alipay.coontroller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import top.echovoid.payment.alipay.service.AlipayService;
import top.echovoid.payment.dto.PaymentDTO;
import top.echovoid.payment.service.PaymentService;

import java.util.Map;

/**
 * @author Penn Collins
 * @since 2025/10/1
 */
@Controller
@AllArgsConstructor
public class AlipayController {
    private final AlipayService alipayService;
    private final PaymentService paymentService;

    /**
     * 支付宝异步通知接口
     *
     */
    @ResponseBody
    @PostMapping("/alipay/notify")
    public String alipayNotifyApi(Map<String, String> map, HttpServletResponse response) {
        boolean isSuccess = alipayService.verify(map);
        if (isSuccess) {
            return "success";
        } else {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return "fail";
        }
    }

    @GetMapping("/alipay/return")
    public String alipayReturnPage(Map<String, String> map, Model model) {
        boolean isSuccess = alipayService.verify(map);
        PaymentDTO paymentDTO = paymentService.query(1L);
        model.addAttribute("payment", paymentDTO);
        model.addAttribute("msg", "支付成功！");
        return "alipay_payment_success";
    }
}
