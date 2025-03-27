package com.service.cart.resources;

import java.util.UUID;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
@Builder
public class CartItemResource {

    private final Integer id;
    private final UUID cartId;
    private final UUID productId;
    private final Integer prodVariantId;
    private final Integer quantity;
}
