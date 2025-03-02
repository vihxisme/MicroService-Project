package com.service.inventory.resources;

import java.util.UUID;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
@Builder
public class InventoryProductResource {

    private final UUID id;
    private final String productCode;
    private final UUID productId;
    private final String productName;
    private final Integer quantity;
    private final Boolean isAllowed;
}
