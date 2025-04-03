package com.service.apicomposition.resources;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Set;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.service.apicomposition.dtos.ProductDetailDTO;
import com.service.apicomposition.dtos.ProductImageDTO;
import com.service.apicomposition.dtos.ProductVariantDTO;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProdWithDiscountAllInfoResource {

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

    @JsonProperty("brand")
    private final String brand;

    @JsonProperty("status")
    private final String status;

    @JsonProperty("productImageUrl")
    private final String productImageUrl;

    @JsonProperty("createdAt")
    private final Timestamp createdAt;

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

    @JsonProperty("productVariantsDTO")
    private final Set<ProductVariantDTO> productVariantsDTO;

    @JsonProperty("productImagesDTO")
    private final Set<ProductImageDTO> productImagesDTO;

    @JsonProperty("productDetailsDTO")
    private final Set<ProductDetailDTO> productDetailsDTO;

    @JsonCreator
    public ProdWithDiscountAllInfoResource(
            @JsonProperty("id") UUID id,
            @JsonProperty("productCode") String productCode,
            @JsonProperty("categorieId") UUID categorieId,
            @JsonProperty("name") String name,
            @JsonProperty("price") BigDecimal price,
            @JsonProperty("brand") String brand,
            @JsonProperty("status") String status,
            @JsonProperty("productImageUrl") String productImageUrl,
            @JsonProperty("createdAt") Timestamp createdAt,
            @JsonProperty("discountPercentage") BigDecimal discountPercentage,
            @JsonProperty("discountAmount") BigDecimal discountAmount,
            @JsonProperty("minOrderValue") BigDecimal minOrderValue,
            @JsonProperty("finalPrice") BigDecimal finalPrice,
            @JsonProperty("targetType") String targetType,
            @JsonProperty("productVariantsDTO") Set<ProductVariantDTO> productVariantsDTO,
            @JsonProperty("productImagesDTO") Set<ProductImageDTO> productImagesDTO,
            @JsonProperty("productDetailsDTO") Set<ProductDetailDTO> productDetailsDTO
    ) {
        this.id = id;
        this.productCode = productCode;
        this.categorieId = categorieId;
        this.name = name;
        this.price = price;
        this.brand = brand;
        this.status = status;
        this.productImageUrl = productImageUrl;
        this.createdAt = createdAt;
        this.discountPercentage = discountPercentage;
        this.discountAmount = discountAmount;
        this.minOrderValue = minOrderValue;
        this.finalPrice = finalPrice;
        this.targetType = targetType;
        this.productVariantsDTO = productVariantsDTO;
        this.productImagesDTO = productImagesDTO;
        this.productDetailsDTO = productDetailsDTO;
    }
}
