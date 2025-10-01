package top.mxzero.payment.alipay.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * 支付宝交易查询响应 DTO
 *
 * @author Peng
 * @since 2025/8/14
 */
@Data
public class AlipayTradeQueryResponseDTO {

    @JsonProperty("alipay_trade_query_response")
    private AlipayTradeQueryData tradeQueryResponse;

    @JsonProperty("sign")
    private String sign;

    @Data
    public static class AlipayTradeQueryData {
        private String code;
        private String msg;

        @JsonProperty("trade_no")
        private String tradeNo;

        @JsonProperty("out_trade_no")
        private String outTradeNo;

        @JsonProperty("buyer_logon_id")
        private String buyerLogonId;

        @JsonProperty("trade_status")
        private String tradeStatus;

        @JsonProperty("total_amount")
        private String totalAmount;

        @JsonProperty("buyer_pay_amount")
        private String buyerPayAmount;

        @JsonProperty("point_amount")
        private String pointAmount;

        @JsonProperty("invoice_amount")
        private String invoiceAmount;

        @JsonProperty("send_pay_date")
        private String sendPayDate;

        @JsonProperty("receipt_amount")
        private String receiptAmount;

        @JsonProperty("store_id")
        private String storeId;

        @JsonProperty("terminal_id")
        private String terminalId;

        @JsonProperty("fund_bill_list")
        private List<FundBill> fundBillList;

        @JsonProperty("store_name")
        private String storeName;

        @JsonProperty("buyer_user_id")
        private String buyerUserId;

        @JsonProperty("buyer_open_id")
        private String buyerOpenId;

        @JsonProperty("discount_amount")
        private String discountAmount;

        @JsonProperty("ext_infos")
        private String extInfos;

        @JsonProperty("buyer_user_type")
        private String buyerUserType;

        @JsonProperty("mdiscount_amount")
        private String mdiscountAmount;
    }

    @Data
    public static class FundBill {
        @JsonProperty("fund_channel")
        private String fundChannel;

        private String amount;

        @JsonProperty("real_amount")
        private String realAmount;
    }
}
