package com.service.order.resources;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.UUID;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
@Builder
public class OrderMnResource {

    private final UUID id;
    private final String orderCode;
    private final UUID userId;
    private final BigDecimal totalAmount;
    private final Integer status;
    private final Timestamp createdAt;
    private final String name;
    private final String email;
}
