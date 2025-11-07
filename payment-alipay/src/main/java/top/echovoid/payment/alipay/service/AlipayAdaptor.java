package top.echovoid.payment.alipay.service;


import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.AlipayConfig;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.*;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.*;
import com.alipay.api.response.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import top.echovoid.common.exceptions.ServiceException;
import top.echovoid.common.utils.IpUtil;
import top.echovoid.payment.alipay.config.AliPayConfigProps;
import top.echovoid.payment.alipay.dto.AlipayTradeQueryResponseDTO;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * AlipayAdaptor 封装了支付宝支付相关的功能，包括电脑支付、手机WAP支付、查询、关闭订单、退款及退款查询。
 *
 * @author Peng
 * @since 2025/8/14
 */
@Slf4j
@Component
public class AlipayAdaptor {
    private final ObjectMapper objectMapper;

    /**
     * 支付宝配置属性（如 appId、网关 URL、回调 URL 等）
     */
    private final AliPayConfigProps configProps;

    /**
     * 商户私钥，用于签名请求
     */
    private final String appPrivateKey;

    /**
     * 支付宝公钥，用于验证支付宝返回的签名
     */
    private final String aliPublicKey;

    /**
     * AlipayClient 客户端，用于执行各种支付宝 API 请求
     */
    private final AlipayClient alipayClient;

    /**
     * 构造函数，通过配置属性和 Spring ResourceLoader 加载密钥，并初始化 AlipayClient
     *
     * @param configProps    支付宝配置信息
     * @param resourceLoader Spring 资源加载器
     * @throws Exception 读取密钥文件失败时抛出
     */
    public AlipayAdaptor(AliPayConfigProps configProps, ResourceLoader resourceLoader, ObjectMapper objectMapper) throws Exception {
        this.objectMapper = objectMapper;
        this.configProps = configProps;
        this.appPrivateKey = resourceLoader.getResource(this.configProps.getAppPrivateKeyPath())
                .getContentAsString(StandardCharsets.UTF_8);
        this.aliPublicKey = resourceLoader.getResource(this.configProps.getAlipayPublicKey())
                .getContentAsString(StandardCharsets.UTF_8);
        this.alipayClient = new DefaultAlipayClient(getAlipayConfig());
    }

    /**
     * 构建支付宝客户端配置
     *
     * @return AlipayConfig
     */
    private AlipayConfig getAlipayConfig() {
        AlipayConfig alipayConfig = new AlipayConfig();
        alipayConfig.setServerUrl(this.configProps.getGatewayUrl());
        alipayConfig.setAppId(configProps.getAppid());
        alipayConfig.setPrivateKey(appPrivateKey);
        alipayConfig.setFormat("json");
        alipayConfig.setAlipayPublicKey(aliPublicKey);
        alipayConfig.setCharset("UTF-8");
        alipayConfig.setSignType("RSA2");
        return alipayConfig;
    }

    /**
     * 扫码枪扫码支付
     *
     * @param outTradeNo 商户订单号
     * @param subjet     订单标题
     * @param amount     支付金额
     * @param authCode   条码
     * @return 支付结果
     * @throws AlipayApiException 支付宝接口异常
     */
    public String pay(String outTradeNo, BigDecimal amount, String subjet, String authCode) throws AlipayApiException {
        AlipayTradePayRequest request = new AlipayTradePayRequest();
        AlipayTradePayModel model = new AlipayTradePayModel();

        model.setOutTradeNo(outTradeNo);
        model.setTotalAmount(amount.toPlainString());
        model.setSubject(subjet);
        model.setAuthCode(authCode);
        model.setScene("bar_code");
        model.setProductCode("FACE_TO_FACE_PAYMENT");

        request.setBizModel(model);
        request.setNotifyUrl(configProps.getNotifyUrl());

        AlipayTradePayResponse response = alipayClient.execute(request);

        if (response.isSuccess()) {
            return response.getBody();
        } else {
            throw new AlipayApiException(response.getBody());
        }
    }


    /**
     * 电脑网站支付（PC端支付）
     *
     * @param outTradeNo 商户订单号
     * @param subjet     订单标题
     * @param amount     支付金额
     * @return 返回支付宝跳转链接（拼接请求参数）
     * @throws AlipayApiException 支付宝接口异常
     */
    public String pagePay(String outTradeNo, String subjet, BigDecimal amount) throws AlipayApiException {
        AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
        AlipayTradePagePayModel model = new AlipayTradePagePayModel();

        model.setOutTradeNo(outTradeNo);
        model.setTotalAmount(amount.toPlainString());
        model.setSubject(subjet);
        model.setProductCode("FAST_INSTANT_TRADE_PAY");

        request.setBizModel(model);

        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes != null) {
            HttpServletRequest httpServletRequest = ((ServletRequestAttributes) requestAttributes).getRequest();
            String accessHost = IpUtil.getAccessHost(httpServletRequest);
            request.setNotifyUrl(accessHost + "/alipay/notify");
        } else {
            request.setNotifyUrl(configProps.getNotifyUrl());
        }


        AlipayTradePagePayResponse response = alipayClient.sdkExecute(request);
        log.info("PC支付响应: {}", response.getBody());

        if (response.isSuccess()) {
            return configProps.getGatewayUrl() + "?" + response.getBody();
        } else {
            throw new AlipayApiException(response.getBody());
        }
    }

    /**
     * 手机网站支付（WAP端支付）
     *
     * @param outTradeNo 商户订单号
     * @param subjet     订单标题
     * @param amount     支付金额
     * @return 返回支付宝跳转链接（拼接请求参数）
     * @throws AlipayApiException 支付宝接口异常
     */
    public String wapPay(String outTradeNo, String subjet, BigDecimal amount) throws AlipayApiException {
        AlipayTradeWapPayRequest request = new AlipayTradeWapPayRequest();
        AlipayTradeWapPayModel model = new AlipayTradeWapPayModel();

        model.setOutTradeNo(outTradeNo);
        model.setTotalAmount(amount.toPlainString());
        model.setSubject(subjet);
        model.setProductCode("FAST_INSTANT_TRADE_PAY");

        request.setBizModel(model);
        request.setNotifyUrl(configProps.getNotifyUrl());

        AlipayTradeWapPayResponse response = alipayClient.sdkExecute(request);
        log.info("WAP支付响应: {}", response.getBody());

        if (response.isSuccess()) {
            return configProps.getGatewayUrl() + "?" + response.getBody();
        } else {
            throw new AlipayApiException(response.getBody());
        }
    }

    /**
     * 查询订单
     *
     * @param outTradeNo 商户订单号
     * @param tradeNo    支付宝交易号
     * @return 返回订单查询结果 JSON
     * @throws AlipayApiException 支付宝接口异常
     */
    public AlipayTradeQueryResponseDTO query(String outTradeNo, String tradeNo) throws AlipayApiException {
        AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
        AlipayTradeQueryModel model = new AlipayTradeQueryModel();

        model.setOutTradeNo(outTradeNo);
        model.setTradeNo(tradeNo);

        request.setBizModel(model);

        AlipayTradeQueryResponse response = alipayClient.execute(request);
        if (response.isSuccess()) {
            try {
                return objectMapper.readValue(response.getBody(), AlipayTradeQueryResponseDTO.class);
            } catch (IOException e) {
                log.error(e.getMessage());
                throw new ServiceException("数据转换错误");
            }
        } else {
            throw new AlipayApiException(response.getBody());
        }
    }

    /**
     * 关闭订单
     *
     * @param outTradeNo 商户订单号
     * @param tradeNo    支付宝交易号
     * @return 返回关闭结果 JSON
     * @throws AlipayApiException 支付宝接口异常
     */
    public String close(String outTradeNo, String tradeNo) throws AlipayApiException {
        AlipayTradeCloseRequest request = new AlipayTradeCloseRequest();
        AlipayTradeCloseModel model = new AlipayTradeCloseModel();

        model.setOutTradeNo(outTradeNo);
        model.setTradeNo(tradeNo);

        request.setBizModel(model);

        AlipayTradeCloseResponse response = alipayClient.execute(request);
        if (response.isSuccess()) {
            return response.getBody();
        } else {
            throw new AlipayApiException(response.getBody());
        }
    }

    /**
     * 退款
     *
     * @param outRequestNo 退款请求号
     * @param refundAmount 退款金额
     * @param outTradeNo   商户订单号
     * @param tradeNo      支付宝交易号
     * @return 返回退款结果 JSON
     * @throws AlipayApiException 支付宝接口异常
     */
    public String refund(String outRequestNo, BigDecimal refundAmount, String outTradeNo, String tradeNo) throws AlipayApiException {
        AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
        AlipayTradeRefundModel model = new AlipayTradeRefundModel();

        model.setOutTradeNo(outTradeNo);
        model.setTradeNo(tradeNo);
        model.setOutRequestNo(outRequestNo);
        model.setRefundAmount(refundAmount.toString());

        request.setBizModel(model);

        AlipayTradeRefundResponse response = alipayClient.execute(request);
        if (response.isSuccess()) {
            return response.getBody();
        } else {
            throw new AlipayApiException(response.getBody());
        }
    }

    /**
     * 查询退款
     *
     * @param outRequestNo 退款请求号
     * @param outTradeNo   商户订单号
     * @param tradeNo      支付宝交易号
     * @return 返回退款查询结果 JSON
     * @throws AlipayApiException 支付宝接口异常
     */
    public String refundQuery(String outRequestNo, String outTradeNo, String tradeNo) throws AlipayApiException {
        AlipayTradeFastpayRefundQueryRequest request = new AlipayTradeFastpayRefundQueryRequest();
        AlipayTradeFastpayRefundQueryModel model = new AlipayTradeFastpayRefundQueryModel();

        model.setOutTradeNo(outTradeNo);
        model.setTradeNo(tradeNo);
        model.setOutRequestNo(outRequestNo);

        request.setBizModel(model);

        AlipayTradeFastpayRefundQueryResponse response = alipayClient.execute(request);
        if (response.isSuccess()) {
            return response.getBody();
        } else {
            throw new AlipayApiException(response.getBody());
        }
    }

    /**
     * 异步通知验签
     *
     * @param paramsMap 参数
     * @param signType  签名类型
     * @return 是否验证成功
     */
    public boolean verify(Map<String, String> paramsMap, String signType) {
        try {
            return AlipaySignature.rsaCheckV1(paramsMap, aliPublicKey, StandardCharsets.UTF_8.name(), signType);
        } catch (AlipayApiException e) {
            log.error(e.getMessage());
            return false;
        }
    }
}