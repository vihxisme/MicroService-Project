package com.service.payment.configurations;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import com.service.payment.utils.VNPayUtil;

import lombok.Getter;

@Configuration
public class VNPAYConfig {

    @Getter
    @Value("${payment.vnpay.url}")
    private String vnp_payUrl;

    @Value("${payment.vnpay.tmnCode}")
    private String vnp_TmnCode;

    @Getter
    @Value("${payment.vnpay.secretKey}")
    private String vnp_secretKey;

    @Value("${payment.vnpay.returnUrl}")
    private String vnp_Returnurl;

    @Value("${payment.vnpay.version}")
    private String vnp_version;

    @Value("${payment.vnpay.command}")
    private String vnp_command;

    @Value("${payment.vnpay.orderType}")
    private String vnp_orderType;

    public Map<String, String> getVNPayConfig() {
        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", this.vnp_version);
        vnp_Params.put("vnp_Command", this.vnp_command);
        vnp_Params.put("vnp_TmnCode", this.vnp_TmnCode);
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_TxnRef", VNPayUtil.getRandomNumber(8));
        vnp_Params.put("vnp_OrderInfo", "Thanh toan don hang:" + VNPayUtil.getRandomNumber(8));
        vnp_Params.put("vnp_OrderType", this.vnp_orderType);
        vnp_Params.put("vnp_Locale", "vn");
        vnp_Params.put("vnp_ReturnUrl", this.vnp_Returnurl);

        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_createDate = formatter.format(calendar.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_createDate);
        calendar.add(Calendar.MINUTE, 15);
        String vnp_expireDate = formatter.format(calendar.getTime());
        vnp_Params.put("vnp_ExpireDate", vnp_expireDate);

        return vnp_Params;
    }
}
