package com.service.inventory.resources;

import java.util.UUID;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
@Builder
public class StockMovementResource {

    private final UUID id;
    private final UUID orderId;
    private final String ordersCode;
    private final UUID productId;
    private final String productCode;
    private final int prodVariantId;
    private final String stockMovementCode;
    private final int movementQuantity;
    private final String movementType;
    private final String movementStatus;
}
