package com.service.apicomposition.resources;

import java.util.UUID;

import com.service.apicomposition.commons.HasProdVariant;
import com.service.apicomposition.commons.HasProduct;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
@Builder
public class StockMvmClientResource implements HasProdVariant, HasProduct {

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
