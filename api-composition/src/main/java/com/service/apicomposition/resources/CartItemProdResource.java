package com.service.apicomposition.resources;

import java.math.BigDecimal;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CartItemProdResource {

    @JsonProperty("id")
    private final Integer id;

    @JsonProperty("cartId")
    private final UUID cartId;

    @JsonProperty("productId")
    private final UUID productId;

    @JsonProperty("productName")
    private final String productName;

    @JsonProperty("price")
    private final BigDecimal price;

    @JsonProperty("prodVariantId")
    private final Integer prodVariantId;

    @JsonProperty("colorId")
    private final Integer colorId;

    @JsonProperty("colorName")
    private final String colorName;

    @JsonProperty("sizeId")
    private final Integer sizeId;

    @JsonProperty("sizeName")
    private final String sizeName;

    @JsonProperty("quantity")
    private final Integer quantity;

    @JsonProperty("productStatus")
    private final String productStatus;

    @JsonCreator
    public CartItemProdResource(
            @JsonProperty("id") Integer id,
            @JsonProperty("cartId") UUID cartId,
            @JsonProperty("productId") UUID productId,
            @JsonProperty("productName") String productName,
            @JsonProperty("price") BigDecimal price,
            @JsonProperty("prodVariantId") Integer prodVariantId,
            @JsonProperty("colorId") Integer colorId,
            @JsonProperty("colorName") String colorName,
            @JsonProperty("sizeId") Integer sizeId,
            @JsonProperty("sizeName") String sizeName,
            @JsonProperty("quantity") Integer quantity,
            @JsonProperty("productStatus") String productStatus
    ) {
        this.id = id;
        this.cartId = cartId;
        this.productId = productId;
        this.productName = productName;
        this.price = price;
        this.prodVariantId = prodVariantId;
        this.colorId = colorId;
        this.colorName = colorName;
        this.sizeId = sizeId;
        this.sizeName = sizeName;
        this.quantity = quantity;
        this.productStatus = productStatus;
    }
}
