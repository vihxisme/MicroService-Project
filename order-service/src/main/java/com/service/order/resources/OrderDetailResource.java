package com.service.order.resources;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Set;
import java.util.UUID;

import com.service.order.entities.ShippingAddress;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
@Builder
public class OrderDetailResource {

    private final UUID id;
    private final String orderCode;
    private final UUID userId;
    private final BigDecimal totalAmount;
    private final BigDecimal shippingFee;
    private final Integer status;
    private final Timestamp createdAt;
    private final Timestamp updatedAt;
    private final Set<OrderItemProdResource> orderItem;
    private final Set<ShippingAddress> shippingAddresses;
}
