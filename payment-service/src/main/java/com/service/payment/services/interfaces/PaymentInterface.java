package com.service.payment.services.interfaces;

import java.util.Map;
import java.util.UUID;

import com.service.payment.entities.Payment;
import com.service.payment.enums.PaymentStatus;
import com.service.payment.requests.PaginationRequest;
import com.service.payment.requests.PaymentRequest;
import com.service.payment.responses.PaginationResponse;

import jakarta.servlet.http.HttpServletRequest;

public interface PaymentInterface {

    Payment createPayment(PaymentRequest request);

    Payment updatePayment(PaymentRequest request);

    Boolean deletePayment(UUID id);

    PaginationResponse<Payment> getPagiPayment(PaginationRequest request);

    Map<PaymentStatus, Long> getPaymentStatusCount();

    String createVnPayUrl(PaymentRequest request, HttpServletRequest httpRequest);

}
