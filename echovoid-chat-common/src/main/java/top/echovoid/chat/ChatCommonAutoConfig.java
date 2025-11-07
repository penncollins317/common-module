package top.echovoid.chat;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author Penn Collins
 * @since 2025/4/23
 */
@Configuration
@MapperScan("top.echovoid.chat.mapper")
public class ChatCommonAutoConfig {
}
