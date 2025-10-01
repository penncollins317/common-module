package top.mxzero.payment.alipay.coontroller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.mxzero.payment.alipay.service.AlipayService;

import java.util.Map;

/**
 * @author Peng
 * @since 2025/10/1
 */
@RestController
@AllArgsConstructor
public class AlipayController {
    private final AlipayService alipayService;

    /**
     * 支付宝异步通知接口
     *
     */
    @RequestMapping("/alipay/notify")
    public String alipayNotifyApi(Map<String, String> map, HttpServletResponse response) {
        boolean isSuccess = alipayService.verify(map);
        if (isSuccess) {
            return "success";
        } else {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return "fail";
        }
    }
}
