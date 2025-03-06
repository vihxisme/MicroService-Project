package com.service.payment.requests;

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
public class PaymentRequest {

    private UUID id;
    private String paymentCode;
    private UUID orderId;
    private UUID userId;
    private Integer paymentMethod;
    private Integer paymentStatus;
    private String message;
}
