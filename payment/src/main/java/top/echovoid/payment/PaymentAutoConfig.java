package top.echovoid.payment;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import top.echovoid.security.core.SecurityConfigProvider;

import java.util.Set;

/**
 * @author Peng
 * @since 2025/10/1
 */
@Configuration
@ComponentScan
@MapperScan("top.echovoid.payment.mapper")
public class PaymentAutoConfig {
    @Bean
    public SecurityConfigProvider paymentSecurityConfigProvider(){
        return new SecurityConfigProvider() {
            @Override
            public Set<String> ignoreUrls() {
                return Set.of("/payment/**");
            }
        };
    }
}
