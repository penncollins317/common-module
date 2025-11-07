package top.echovoid.product;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author Peng
 * @since 2025/5/10
 */
@Configuration
@ComponentScan
@MapperScan("top.echovoid.product.mapper")
public class ProductServiceAutoConfig {
}

