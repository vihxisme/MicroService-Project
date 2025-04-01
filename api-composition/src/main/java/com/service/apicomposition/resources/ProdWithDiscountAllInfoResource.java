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

    @JsonProperty("product_code")
    private final String productCode;

    @JsonProperty("categorie_id")
    private final UUID categorieId;

    @JsonProperty("name")
    private final String name;

    @JsonProperty("price")
    private final BigDecimal price;

    @JsonProperty("brand")
    private final String brand;

    @JsonProperty("status")
    private final String status;

    @JsonProperty("product_image_url")
    private final String productImageUrl;

    @JsonProperty("created_at")
    private final Timestamp createdAt;

    @JsonProperty("discount_percentage")
    private final BigDecimal discountPercentage;

    @JsonProperty("discount_amount")
    private final BigDecimal discountAmount;

    @JsonProperty("min_order_value")
    private final BigDecimal minOrderValue;

    @JsonProperty("final_price")
    private final BigDecimal finalPrice;

    @JsonProperty("target_type")
    private final String targetType;

    @JsonProperty("product_variants_dto")
    private final Set<ProductVariantDTO> productVariantsDTO;

    @JsonProperty("product_images_dto")
    private final Set<ProductImageDTO> productImagesDTO;

    @JsonProperty("product_details_dto")
    private final Set<ProductDetailDTO> productDetailsDTO;

    @JsonCreator
    public ProdWithDiscountAllInfoResource(
            @JsonProperty("id") UUID id,
            @JsonProperty("product_code") String productCode,
            @JsonProperty("categorie_id") UUID categorieId,
            @JsonProperty("name") String name,
            @JsonProperty("price") BigDecimal price,
            @JsonProperty("brand") String brand,
            @JsonProperty("status") String status,
            @JsonProperty("product_image_url") String productImageUrl,
            @JsonProperty("created_at") Timestamp createdAt,
            @JsonProperty("discount_percentage") BigDecimal discountPercentage,
            @JsonProperty("discount_amount") BigDecimal discountAmount,
            @JsonProperty("min_order_value") BigDecimal minOrderValue,
            @JsonProperty("final_price") BigDecimal finalPrice,
            @JsonProperty("target_type") String targetType,
            @JsonProperty("product_variants_dto") Set<ProductVariantDTO> productVariantsDTO,
            @JsonProperty("product_images_dto") Set<ProductImageDTO> productImagesDTO,
            @JsonProperty("product_details_dto") Set<ProductDetailDTO> productDetailsDTO
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
