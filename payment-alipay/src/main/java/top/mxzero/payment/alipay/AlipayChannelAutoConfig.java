package top.mxzero.payment.alipay;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import top.mxzero.payment.PaymentAdaptorRegisterBean;
import top.mxzero.payment.PaymentChannel;
import top.mxzero.payment.alipay.config.AliPayConfigProps;
import top.mxzero.payment.alipay.service.AlipayPaymentChannelAdaptor;
import top.mxzero.security.core.SecurityConfigProvider;

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

