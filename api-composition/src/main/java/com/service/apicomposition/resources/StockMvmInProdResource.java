package com.service.apicomposition.resources;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.service.apicomposition.commons.HasProdVariant;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class StockMvmInProdResource implements HasProdVariant {

    @JsonProperty("id")
    private final UUID id;

    @JsonProperty("productId")
    private final UUID productId;

    @JsonProperty("productCode")
    private final String productCode;

    @JsonProperty("productName")
    private final String productName;

    @JsonProperty("prodVariantId")
    private final int prodVariantId;

    @JsonProperty("colorId")
    private final int colorId;

    @JsonProperty("colorName")
    private final String colorName;

    @JsonProperty("sizeId")
    private final int sizeId;

    @JsonProperty("sizeName")
    private final String sizeName;

    @JsonProperty("stockMovementCode")
    private final String stockMovementCode;

    @JsonProperty("movementQuantity")
    private final int movementQuantity;

    @JsonProperty("movementType")
    private final String movementType;

    @JsonProperty("movementStatus")
    private final String movementStatus;

    @JsonCreator
    public StockMvmInProdResource(
            @JsonProperty("id") UUID id,
            @JsonProperty("productId") UUID productId,
            @JsonProperty("productCode") String productCode,
            @JsonProperty("productName") String productName,
            @JsonProperty("prodVariantId") int prodVariantId,
            @JsonProperty("colorId") int colorId,
            @JsonProperty("colorName") String colorName,
            @JsonProperty("sizeId") int sizeId,
            @JsonProperty("sizeName") String sizeName,
            @JsonProperty("stockMovementCode") String stockMovementCode,
            @JsonProperty("movementQuantity") int movementQuantity,
            @JsonProperty("movementType") String movementType,
            @JsonProperty("movementStatus") String movementStatus
    ) {
        this.id = id;
        this.productId = productId;
        this.productCode = productCode;
        this.productName = productName;
        this.prodVariantId = prodVariantId;
        this.colorId = colorId;
        this.colorName = colorName;
        this.sizeId = sizeId;
        this.sizeName = sizeName;
        this.stockMovementCode = stockMovementCode;
        this.movementQuantity = movementQuantity;
        this.movementType = movementType;
        this.movementStatus = movementStatus;
    }

}
