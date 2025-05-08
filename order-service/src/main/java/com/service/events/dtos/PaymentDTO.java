package com.service.events.dtos;

import java.math.BigDecimal;
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
public class PaymentDTO {

    private UUID id;
    private String paymentCode;
    private String orderCode;
    private UUID orderId;
    private UUID userId;

    private BigDecimal totalAmount;
    ;

    @Builder.Default
    private Integer paymentMethod = 3;

    @Builder.Default
    private Integer paymentStatus = 1;

    @Builder.Default
    private String message = "Thanh toán đơn hàng";
}
