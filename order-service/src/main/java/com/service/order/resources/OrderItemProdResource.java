package com.service.order.resources;

import java.math.BigDecimal;
import java.util.UUID;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
@Builder
public class OrderItemProdResource {

    private final Integer id;
    private final UUID productId;
    private final Integer prodVariantId;
    private final String productCode;
    private final String productName;
    private final String colorName;
    private final String sizeName;
    private final String imageVariant;
    private final Integer quantity;
    private final BigDecimal price;
    private final BigDecimal totalPrice;
}
