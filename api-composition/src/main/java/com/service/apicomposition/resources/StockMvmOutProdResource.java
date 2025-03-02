package com.service.apicomposition.resources;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.service.apicomposition.commons.HasProdVariant;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class StockMvmOutProdResource implements HasProdVariant {

    @JsonProperty("id")
    private final UUID id;

    @JsonProperty("order_id")
    private final UUID orderId;

    @JsonProperty("orders_code")
    private final String ordersCode;

    @JsonProperty("product_id")
    private final UUID productId;

    @JsonProperty("product_code")
    private final String productCode;

    @JsonProperty("product_name")
    private final String productName;

    @JsonProperty("prod_variant_id")
    private final int prodVariantId;

    @JsonProperty("color_id")
    private final int colorId;

    @JsonProperty("color_name")
    private final String colorName;

    @JsonProperty("size_id")
    private final int sizeId;

    @JsonProperty("size_name")
    private final String sizeName;

    @JsonProperty("stock_movement_code")
    private final String stockMovementCode;

    @JsonProperty("movement_quantity")
    private final int movementQuantity;

    @JsonProperty("movement_type")
    private final String movementType;

    @JsonProperty("movement_status")
    private final String movementStatus;

    @JsonCreator
    public StockMvmOutProdResource(
            @JsonProperty("id") UUID id,
            @JsonProperty("order_id") UUID orderId,
            @JsonProperty("orders_code") String ordersCode,
            @JsonProperty("product_id") UUID productId,
            @JsonProperty("product_code") String productCode,
            @JsonProperty("product_name") String productName,
            @JsonProperty("prod_variant_id") int prodVariantId,
            @JsonProperty("color_id") int colorId,
            @JsonProperty("color_name") String colorName,
            @JsonProperty("size_id") int sizeId,
            @JsonProperty("size_name") String sizeName,
            @JsonProperty("stock_movement_code") String stockMovementCode,
            @JsonProperty("movement_quantity") int movementQuantity,
            @JsonProperty("movement_type") String movementType,
            @JsonProperty("movement_status") String movementStatus
    ) {
        this.id = id;
        this.orderId = orderId;
        this.ordersCode = ordersCode;
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
