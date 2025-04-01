package com.service.apicomposition.resources;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Set;
import java.util.UUID;

import com.service.apicomposition.commons.HasProdInfo;
import com.service.apicomposition.dtos.ProductDetailDTO;
import com.service.apicomposition.dtos.ProductImageDTO;
import com.service.apicomposition.dtos.ProductVariantDTO;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
@Builder
public class ProdAllInfoResource implements HasProdInfo {

    private final UUID id;
    private final String productCode;
    private final UUID categorieId;
    private final String name;
    private final BigDecimal price;
    private final String brand;
    private final String status;
    private final String productImageUrl;
    private final Timestamp createdAt;
    private final Set<ProductVariantDTO> productVariantsDTO;
    private final Set<ProductImageDTO> productImagesDTO;
    private final Set<ProductDetailDTO> productDetailsDTO;

}
