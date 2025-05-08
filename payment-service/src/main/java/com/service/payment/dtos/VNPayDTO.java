package com.service.payment.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class VNPayDTO {

    private String vnp_ResponseCode; // vnp_ResponseCode
    private String vnp_TxnRef; // vnp_TxnRef
    private String vnp_Amount; // vnp_Amount / 100
    private String vnp_OrderInfo; // vnp_OrderInfo
    private String vnp_TransactionNo; // vnp_TransactionNo
    private String vnp_TransactionStatus; // vnp_TransactionStatus
    private String vnp_BankCode; // vnp_BankCode
    private String vnp_CardType; // vnp_CardType
    private String vnp_PayDate; // vnp_PayDate
    private String vnp_IpAddr; // vnp_IpAddr
}
