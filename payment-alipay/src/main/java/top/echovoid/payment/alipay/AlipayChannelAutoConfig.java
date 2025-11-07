package top.echovoid.payment.alipay;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import top.echovoid.payment.PaymentAdaptorRegisterBean;
import top.echovoid.payment.PaymentChannel;
import top.echovoid.payment.alipay.config.AliPayConfigProps;
import top.echovoid.payment.alipay.service.AlipayPaymentChannelAdaptor;
import top.echovoid.security.core.SecurityConfigProvider;

import java.util.Set;

/**
 * @author Peng
 * @since 2025/10/1
 */
@Configuration
@ComponentScan
@EnableConfigurationProperties(AliPayConfigProps.class)
public class AlipayChannelAutoConfig {
    @Bean
    public SecurityConfigProvider AlipaySecurityConfigProvider() {
        return new SecurityConfigProvider() {
            @Override
            public Set<String> ignoreUrls() {
                return Set.of("/alipay/notify", "/test/alipay/**");
            }
        };
    }

    @Bean
    public PaymentAdaptorRegisterBean alipayPaymentAdaptorRegisterBean(AlipayPaymentChannelAdaptor alipayPaymentChannelAdaptor) {
        return new PaymentAdaptorRegisterBean(PaymentChannel.ALIPAY, alipayPaymentChannelAdaptor);
    }
}

