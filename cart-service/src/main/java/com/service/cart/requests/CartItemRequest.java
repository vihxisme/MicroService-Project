package com.service.cart.requests;

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
public class CartItemRequest {

    private Integer id;
    private UUID cartId;
    private UUID productId;
    private Integer prodVariantId;
    private Integer quantity;
}
