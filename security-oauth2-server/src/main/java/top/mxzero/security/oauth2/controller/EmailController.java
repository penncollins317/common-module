package top.mxzero.security.oauth2.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.mxzero.common.dto.RestData;
import top.mxzero.security.oauth2.service.EmailService;

import java.util.Random;

@Slf4j
@RestController
@AllArgsConstructor
public class EmailController {
    private final EmailService emailService;

    /**
     * 发送验证码邮件测试接口
     * URL 示例：/send?to=example@example.com
     */
    @GetMapping("/send")
    public RestData<String> sendEmail(@RequestParam @Valid @Email String to) {
        // 生成6位验证码
        String code = generateSixDigitCode();
        log.debug("Email code:{}", code);
        try {
            emailService.sendVerificationEmail(to, code);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return RestData.success("验证码已发送");
    }

    private String generateSixDigitCode() {
        Random random = new Random();
        int number = 100000 + random.nextInt(900000);
        return String.valueOf(number);
    }
}
