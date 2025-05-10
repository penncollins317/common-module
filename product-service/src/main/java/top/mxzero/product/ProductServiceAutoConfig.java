package top.mxzero.product;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author Peng
 * @since 2025/5/10
 */
@Configuration
@ComponentScan
@MapperScan("top.mxzero.product.mapper")
public class ProductServiceAutoConfig {
}

