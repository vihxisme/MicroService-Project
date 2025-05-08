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
public class TopProductResource {

    private final UUID id;
    private final String productCode;
    private final String name;
    private final UUID categorieId;
    private final String categoryName;
    private final BigDecimal price;
    private final String status;
    private final String productImageUrl;
    private final Timestamp createdAt;
    private final BigDecimal totalRevenue;
}
