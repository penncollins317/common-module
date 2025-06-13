package top.mxzero.chat;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author Peng
 * @since 2025/4/23
 */
@Configuration
@MapperScan("top.mxzero.chat.mapper")
public class ChatCommonAutoConfig {
}
