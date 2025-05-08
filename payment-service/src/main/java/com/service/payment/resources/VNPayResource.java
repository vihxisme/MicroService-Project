package com.service.payment.resources;

import java.sql.Timestamp;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
@Builder
public class VNPayResource {

    private final String vnp_txnRef;
    private final String vnp_orderInfo;
    private final String vnp_amount;
    private final String vnp_ipAddr;
    private final String vnp_transactionStatus;
    private final String vnp_bankCode;
    private final String vnp_cardType;
    private final String vnp_responseCode;
    private final String vnp_transactionNo;
    private final Timestamp vnp_payDate;
}
