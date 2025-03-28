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
public class InventoryItemRequest {

    private UUID id;
    private UUID inventoryId;
    private Integer prodVariantId;

    @Builder.Default
    private Integer itemQuantity = 0;
}
