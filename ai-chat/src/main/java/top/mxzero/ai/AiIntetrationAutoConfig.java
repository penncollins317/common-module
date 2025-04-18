package top.mxzero.ai;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author Peng
 * @since 2024/11/27
 */
@Configuration
@ComponentScan
@MapperScan("top.mxzero.ai.mapper")
public class AiIntetrationAutoConfig {
}