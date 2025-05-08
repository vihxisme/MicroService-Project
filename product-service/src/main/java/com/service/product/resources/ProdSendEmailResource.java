package com.service.product.resources;

import java.util.UUID;
import java.math.BigDecimal;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
@Builder
public class ProdSendEmailResource {

    private final UUID productId;
    private final Integer variantId;
    private final String name;
    private final BigDecimal price;
    private final String imageUrl;
    private final String colorName;
    private final String sizeName;
}
