package top.mxzero.chat;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author Peng
 * @since 2025/4/23
 */
@Configuration
@ComponentScan
@MapperScan("top.mxzero.chat.mapper")
public class ChatCommonAutoConfig {
}
