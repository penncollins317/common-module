package top.echovoid.payment;


import jakarta.annotation.PostConstruct;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Penn Collins
 * @since 2025/10/2
 */
@Component
public class PaymentChannelRegistry {

    private final ApplicationContext context;
    private final Map<String, PaymentChannelAdaptor> adaptorMap = new ConcurrentHashMap<>();

    public PaymentChannelRegistry(ApplicationContext context) {
        this.context = context;
    }

    @PostConstruct
    public void init() {
        // 扫描所有 PaymentChannelAdaptorRegisterBean Bean
        Map<String, PaymentAdaptorRegisterBean> beans = context.getBeansOfType(PaymentAdaptorRegisterBean.class);
        beans.values().forEach(bean -> {
            adaptorMap.put(bean.getChannel().getCode(), bean.getAdaptor());
        });
    }

    public PaymentChannelAdaptor getAdaptor(String code) {
        PaymentChannelAdaptor adaptor = adaptorMap.get(code);
        if (adaptor == null) {
            throw new IllegalArgumentException("Unsupported payment channel: " + code);
        }
        return adaptor;
    }
}