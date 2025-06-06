package com.service.apicomposition.resources;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.UUID;

import com.service.apicomposition.commons.HasProdInfo;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
@Builder
public class ProductClientResource implements HasProdInfo {

    private final UUID id;
    private final String productCode;
    private final UUID categorieId;
    private final String name;
    private final BigDecimal price;
    private final String brand;
    private final String status;
    private final String productImageUrl;
    private final Timestamp createdAt;
}
