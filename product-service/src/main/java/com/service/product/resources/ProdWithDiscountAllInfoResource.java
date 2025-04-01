package com.service.product.resources;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Set;
import java.util.UUID;

import com.service.product.dtos.ProductDetailDTO;
import com.service.product.dtos.ProductImageDTO;
import com.service.product.dtos.ProductVariantDTO;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
@Builder
public class ProdWithDiscountAllInfoResource {

    private final UUID id;
    private final String productCode;
    private final UUID categorieId;
    private final String name;
    private final BigDecimal price;
    private final String brand;
    private final String status;
    private final String productImageUrl;
    private final Timestamp createdAt;
    private final BigDecimal discountPercentage;
    private final BigDecimal discountAmount;
    private final BigDecimal minOrderValue;
    private final BigDecimal finalPrice;
    private final String targetType;
    private final Set<ProductVariantDTO> productVariantsDTO;
    private final Set<ProductImageDTO> productImagesDTO;
    private final Set<ProductDetailDTO> productDetailsDTO;
}
