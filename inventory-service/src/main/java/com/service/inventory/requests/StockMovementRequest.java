package com.service.inventory.requests;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class StockMovementRequest {

    private UUID id;
    private UUID inventoryItemId;
    private UUID orderId;
    private String ordersCode;
    private String stockMovementCode;
    private Integer movementQuantity;
    private String movementType;

    @Builder.Default
    private String movementStatus = "PENDING";
}
