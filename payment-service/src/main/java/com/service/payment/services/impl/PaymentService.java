package com.service.payment.services.impl;

import java.util.Random;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.service.payment.entities.Payment;
import com.service.payment.mappers.PaymentMapper;
import com.service.payment.repositories.PaymentRepository;
import com.service.payment.requests.PaymentRequest;
import com.service.payment.services.interfaces.PaymentInterface;

import jakarta.persistence.EntityNotFoundException;

@Service
public class PaymentService implements PaymentInterface {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private PaymentMapper paymentMapper;

    private Logger logger = LoggerFactory.getLogger(PaymentService.class);

    @Override
    public Payment createPayment(PaymentRequest request) {
        if (request.getPaymentCode() == null) {
            request.setPaymentCode(generatePaymentCode());
        }

        Payment payment = paymentMapper.toPayment(request);

        return paymentRepository.save(payment);
    }

    @Override
    public Payment updatePayment(PaymentRequest request) {
        Payment existPayment = paymentRepository.findById(request.getId()).orElseThrow(()
                -> new EntityNotFoundException("Payment not found"));

        paymentMapper.updatePaymentFromRequest(request, existPayment);

        return paymentRepository.save(existPayment);
    }

    @Override
    public Boolean deletePayment(UUID id) {
        Payment existPayment = paymentRepository.findById(id).orElseThrow(()
                -> new EntityNotFoundException("Payment not found"));

        paymentRepository.delete(existPayment);

        return true;
    }

    private String generatePaymentCode() {
        Boolean isUnique;
        Random random = new Random();
        String code;
        do {
            int randomNumberStart = random.nextInt(100, 999);
            int randomNumberMiddle = random.nextInt(1000, 9999);
            int randomNumberEnd = random.nextInt(1000, 9999);
            code = String.format("%03d-%05d-%05d", randomNumberStart, randomNumberMiddle, randomNumberEnd);
            isUnique = !paymentRepository.existsByPaymentCode(code);
        } while (!isUnique);

        return code;
    }

}
