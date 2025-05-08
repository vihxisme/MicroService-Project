package com.service.payment.requests;

import java.sql.Timestamp;
import java.util.UUID;

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
public class VNPayTransactionRequest {

    private UUID paymentId; // payment_id
    private String transactionNo; // vnp_TransactionNo
    private Long amount; // vnp_Amount / 100
    private String status; // "PAID" hoặc "FAILED"
    private String responseCode; // vnp_ResponseCode
    private Timestamp payDate; // vnp_PayDate
    private String bankCode; // vnp_BankCode
    private String cardType; // vnp_CardType
    private String bankTranNo; // vnp_BankTranNo
    private String orderInfo; // vnp_OrderInfo
}
