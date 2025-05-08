package com.service.payment.mappers;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.service.payment.entities.Payment;
import com.service.payment.repositories.PaymentRepository;

import jakarta.persistence.EntityNotFoundException;

@Component
public class Convert {

    @Autowired
    private PaymentRepository paymentRepository;

    @Named("paymentIdToPayment")
    public Payment paymentIdToPayment(UUID paymentId) {
        return paymentRepository.findById(paymentId).orElseThrow(() -> new EntityNotFoundException("Payment not found"));
    }

    @Named("convertToTimestamp")
    public Timestamp toTimestamp(String date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        try {
            Date parsedDate = format.parse(date);
            return new Timestamp(parsedDate.getTime());
        } catch (ParseException e) {
            throw new RuntimeException("Failed to parse date: " + date, e);
        }
    }
}
