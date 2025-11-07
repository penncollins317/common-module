package top.echovoid.common.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import top.echovoid.common.service.SmsSender;

/**
 * @author Penn Collins
 * @since 2024/10/5
 */
@Slf4j
public class ConsoleSmsSender implements SmsSender {
    @Override
    @Async
    public void sendSmsAction(String phone, String code) {
        log.info("sms code:{}:{}", phone, code);
    }
}
