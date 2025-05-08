package com.service.payment.controllers;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.service.payment.dtos.VNPayDTO;
import com.service.payment.entities.Payment;
import com.service.payment.repositories.PaymentRepository;
import com.service.payment.requests.VNPayTransactionRequest;
import com.service.payment.responses.SuccessResponse;
import com.service.payment.services.interfaces.VNPayTransactionInterface;

import jakarta.persistence.EntityNotFoundException;

@RestController
@RequestMapping("/v1/vnpay")
public class VNPayTransactionController {

    @Autowired
    private VNPayTransactionInterface vnPayTransactionInterface;

    @Autowired
    private PaymentRepository paymentRepository;

    @PostMapping("/transaction/create")
    public ResponseEntity<?> createVNPayTransaction(
            @RequestBody VNPayDTO dto) throws ParseException {
        Payment payment = paymentRepository.findByPaymentCode(dto.getVnp_TxnRef());
        if (payment == null) {
            throw new EntityNotFoundException("Payment not found");
        }

        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = formatter.parse(dto.getVnp_PayDate());

        VNPayTransactionRequest request = VNPayTransactionRequest.builder()
                .paymentId(payment.getId())
                .transactionNo(dto.getVnp_TransactionNo())
                .amount(Long.parseLong(dto.getVnp_Amount()) / 100)
                .status(dto.getVnp_TransactionStatus())
                .responseCode(dto.getVnp_ResponseCode())
                .payDate(new Timestamp(date.getTime()))
                .bankCode(dto.getVnp_BankCode())
                .cardType(dto.getVnp_CardType())
                .orderInfo(dto.getVnp_OrderInfo())
                .build();

        return ResponseEntity.ok(
                new SuccessResponse<>(
                        "SUCCESS",
                        vnPayTransactionInterface.createVNPayTransaction(request)));
    }
}
