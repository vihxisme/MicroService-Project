package com.service.inventory.resources;

import java.util.UUID;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
@Builder
public class InventoryResource {

    private final UUID id;
    private final String productCode;
    private final UUID productId;
    private final Integer quantity;
    private final Boolean isAllowed;
}
