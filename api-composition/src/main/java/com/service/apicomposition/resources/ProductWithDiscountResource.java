package com.service.apicomposition.resources;

import java.math.BigDecimal;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ProductWithDiscountResource {

    @JsonProperty("id")
    private final UUID id;

    @JsonProperty("productCode")
    private final String productCode;

    @JsonProperty("categorieId")
    private final UUID categorieId;

    @JsonProperty("name")
    private final String name;

    @JsonProperty("price")
    private final BigDecimal price;

    @JsonProperty("status")
    private final String status;

    @JsonProperty("productImageUrl")
    private final String productImageUrl;

    @JsonProperty("discountPercentage")
    private final BigDecimal discountPercentage;

    @JsonProperty("discountAmount")
    private final BigDecimal discountAmount;

    @JsonProperty("minOrderValue")
    private final BigDecimal minOrderValue;

    @JsonProperty("finalPrice")
    private final BigDecimal finalPrice;

    @JsonProperty("targetType")
    private final String targetType;

    @JsonCreator
    public ProductWithDiscountResource(
            @JsonProperty("id") UUID id,
            @JsonProperty("productCode") String productCode,
            @JsonProperty("categorieId") UUID categorieId,
            @JsonProperty("name") String name,
            @JsonProperty("price") BigDecimal price,
            @JsonProperty("status") String status,
            @JsonProperty("productImageUrl") String productImageUrl,
            @JsonProperty("discountPercentage") BigDecimal discountPercentage,
            @JsonProperty("discountAmount") BigDecimal discountAmount,
            @JsonProperty("minOrderValue") BigDecimal minOrderValue,
            @JsonProperty("finalPrice") BigDecimal finalPrice,
            @JsonProperty("targetType") String targetType) {
        this.id = id;
        this.productCode = productCode;
        this.categorieId = categorieId;
        this.name = name;
        this.price = price;
        this.status = status;
        this.productImageUrl = productImageUrl;
        this.discountPercentage = discountPercentage;
        this.discountAmount = discountAmount;
        this.minOrderValue = minOrderValue;
        this.finalPrice = finalPrice;
        this.targetType = targetType;
    }
}
