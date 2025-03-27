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
public class OrderItemRequest {

    private Integer id;
    private UUID orderId;
    private UUID productId;
    private Integer prodVariantId;
    private Integer quantity;
    private BigDecimal price;
    private BigDecimal totalPrice;
}
