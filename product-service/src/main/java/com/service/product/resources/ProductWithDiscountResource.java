package com.service.product.resources;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.UUID;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
@Builder
public class ProductWithDiscountResource {

    private final UUID id;
    private final String productCode;
    private final UUID categorieId;
    private final String name;
    private final BigDecimal price;
    private final String status;
    private final String productImageUrl;
    private final BigDecimal discountPercentage;
    private final BigDecimal discountAmount;
    private final BigDecimal minOrderValue;
    private final BigDecimal finalPrice;
    private final String targetType;
    private final Timestamp createdAt;
}
