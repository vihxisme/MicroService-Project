package com.service.apicomposition.resources;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class InventoryProdResource {

    @JsonProperty("id")
    private final UUID id;

    @JsonProperty("productCode")
    private final String productCode;

    @JsonProperty("productId")
    private final UUID productId;

    @JsonProperty("productName")
    private final String productName;

    @JsonProperty("categoryName")
    private final String categoryName;

    @JsonProperty("productImageUrl")
    private final String productImageUrl;

    @JsonProperty("quantity")
    private final Integer quantity;

    @JsonProperty("isAllowed")
    private final Boolean isAllowed;

    @JsonCreator
    public InventoryProdResource(
            @JsonProperty("id") UUID id,
            @JsonProperty("productCode") String productCode,
            @JsonProperty("productId") UUID productId,
            @JsonProperty("productName") String productName,
            @JsonProperty("categoryName") String categoryName,
            @JsonProperty("productImageUrl") String productImageUrl,
            @JsonProperty("quantity") Integer quantity,
            @JsonProperty("isAllowed") Boolean isAllowed) {
        this.id = id;
        this.productCode = productCode;
        this.productId = productId;
        this.productName = productName;
        this.categoryName = categoryName;
        this.productImageUrl = productImageUrl;
        this.quantity = quantity;
        this.isAllowed = isAllowed;
    }

}
