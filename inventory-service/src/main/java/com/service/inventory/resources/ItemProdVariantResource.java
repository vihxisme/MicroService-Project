package com.service.inventory.resources;

import java.util.UUID;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
@Builder
public class ItemProdVariantResource {

    private final UUID id;
    private final UUID inventoryId;
    private final int colorId;
    private final String colorName;
    private final int sizeId;
    private final String sizeName;
    private final int prodVariantId;
    private final int itemQuantity;
}
