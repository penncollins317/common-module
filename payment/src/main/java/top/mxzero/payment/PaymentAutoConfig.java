package top.mxzero.payment;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author Peng
 * @since 2025/10/1
 */
@Configuration
@ComponentScan
@MapperScan("top.mxzero.payment.mapper")
public class PaymentAutoConfig {

}
