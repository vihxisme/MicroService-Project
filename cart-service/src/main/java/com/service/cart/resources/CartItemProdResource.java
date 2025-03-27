package com.service.cart.resources;

import java.math.BigDecimal;
import java.util.UUID;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
@Builder
public class CartItemProdResource {

    private final Integer id;
    private final UUID cartId;
    private final UUID productId;
    private final String productName;
    private final BigDecimal price;
    private final Integer prodVariantId;
    private final Integer colorId;
    private final String colorName;
    private final Integer sizeId;
    private final String sizeName;
    private final Integer quantity;
    private final String productStatus;
}
