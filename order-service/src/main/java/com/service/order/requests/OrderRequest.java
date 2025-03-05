package com.service.order.requests;

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
public class OrderRequest {

    private UUID id;
    private String orderCode;
    private UUID userId;
    private BigDecimal totalAmount;
    private BigDecimal shippingFee;
    private Integer status;
}
