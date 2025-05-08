package com.service.payment.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.service.payment.entities.Payment;
import com.service.payment.entities.VNPayTransaction;
import com.service.payment.mappers.VNPayTransactionMapper;
import com.service.payment.repositories.PaymentRepository;
import com.service.payment.repositories.VNPayTransactionRepository;
import com.service.payment.requests.VNPayTransactionRequest;
import com.service.payment.services.interfaces.VNPayTransactionInterface;

import jakarta.transaction.Transactional;

@Service
public class VNPayTransactionService implements VNPayTransactionInterface {

    @Autowired
    private VNPayTransactionRepository vnPayTransactionRepository;

    @Autowired
    private VNPayTransactionMapper vnPayTransactionMapper;

    @Autowired
    private PaymentRepository paymentRepository;

    @Override
    @Transactional
    public VNPayTransaction createVNPayTransaction(VNPayTransactionRequest request) {
        VNPayTransaction vnPayTransaction = vnPayTransactionMapper.toVNPayTransaction(request);

        if (request.getStatus().equals("00")) {
            vnPayTransaction.setStatus("PAID");

            Payment payment = vnPayTransaction.getPayment();
            payment.setMessage("Thanh toán thành công");
            payment.setPaymentStatus(2);

            paymentRepository.save(payment);
        } else {
            vnPayTransaction.setStatus("FAILED");
        }

        return vnPayTransactionRepository.save(vnPayTransaction);
    }

}
