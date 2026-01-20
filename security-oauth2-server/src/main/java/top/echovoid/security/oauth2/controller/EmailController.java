package top.echovoid.security.oauth2.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.echovoid.common.dto.RestData;
import top.echovoid.security.oauth2.service.EmailService;

import java.util.Random;

@Tag(name = "邮件接口", description = "提供发送验证码邮件等功能")
@Slf4j
@RestController
@AllArgsConstructor
public class EmailController {
    private final EmailService emailService;

    /**
     * 发送验证码邮件测试接口
     * URL 示例：/send?to=example@example.com
     */
    @Operation(summary = "发送验证码邮件", description = "向指定的电子邮箱发送 6 位数字验证码")
    @GetMapping("/send")
    public RestData<String> sendEmail(@Parameter(description = "收件人邮箱地址") @RequestParam @Valid @Email String to) {
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
