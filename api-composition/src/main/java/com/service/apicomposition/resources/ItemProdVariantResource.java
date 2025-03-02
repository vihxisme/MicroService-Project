package com.service.apicomposition.resources;

import java.io.Serializable;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ItemProdVariantResource implements Serializable {

    @JsonProperty("id")
    private final UUID id;

    @JsonProperty("inventoryId")
    private final UUID inventoryId;

    @JsonProperty("colorId")
    private final int colorId;

    @JsonProperty("colorName")
    private final String colorName;

    @JsonProperty("sizeId")
    private final int sizeId;

    @JsonProperty("sizeName")
    private final String sizeName;

    @JsonProperty("prodVariantId")
    private final int prodVariantId;

    @JsonProperty("itemQuantity")
    private final int itemQuantity;

    @JsonCreator
    public ItemProdVariantResource(
            @JsonProperty("id") UUID id,
            @JsonProperty("inventoryId") UUID inventoryId,
            @JsonProperty("colorId") int colorId,
            @JsonProperty("colorName") String colorName,
            @JsonProperty("sizeId") int sizeId,
            @JsonProperty("sizeName") String sizeName,
            @JsonProperty("prodVariantId") int prodVariantId,
            @JsonProperty("itemQuantity") int itemQuantity) {
        this.id = id;
        this.inventoryId = inventoryId;
        this.colorId = colorId;
        this.colorName = colorName;
        this.sizeId = sizeId;
        this.sizeName = sizeName;
        this.prodVariantId = prodVariantId;
        this.itemQuantity = itemQuantity;
    }

}
