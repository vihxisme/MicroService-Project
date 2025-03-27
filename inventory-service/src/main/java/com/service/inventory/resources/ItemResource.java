package com.service.inventory.resources;

import java.util.UUID;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
@Builder
public class ItemResource {

    private final UUID id;
    private final UUID inventoryId;
    private final int prodVariantId;
    private final int itemQuantity;
}
