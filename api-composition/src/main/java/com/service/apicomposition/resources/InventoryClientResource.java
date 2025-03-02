package com.service.apicomposition.resources;

import java.util.UUID;

import com.service.apicomposition.commons.HasProduct;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
@Builder
public class InventoryClientResource implements HasProduct {

    private final UUID id;
    private final String productCode;
    private final UUID productId;
    private final Integer quantity;
    private final Boolean isAllowed;
}
