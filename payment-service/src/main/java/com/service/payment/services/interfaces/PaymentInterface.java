package com.service.payment.services.interfaces;

import java.util.UUID;

import com.service.payment.entities.Payment;
import com.service.payment.requests.PaymentRequest;

public interface PaymentInterface {

    Payment createPayment(PaymentRequest request);

    Payment updatePayment(PaymentRequest request);

    Boolean deletePayment(UUID id);
}
