package top.mxzero.service.user;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author Peng
 * @since 2025/4/4
 */
@MapperScan("top.mxzero.service.user.mapper")
@Configuration
@ComponentScan
public class UserServiceAutoConfig {
}
