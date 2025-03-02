package com.service.apicomposition.resources;

import java.util.UUID;

import com.service.apicomposition.commons.HasProdVariant;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
@Builder
public class ItemClientResource implements HasProdVariant {

    private final UUID id;
    private final UUID inventoryId;
    private final int prodVariantId;
    private final int itemQuantity;
}
