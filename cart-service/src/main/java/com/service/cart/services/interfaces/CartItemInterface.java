package com.service.cart.services.interfaces;

import java.util.List;
import java.util.UUID;

import com.service.cart.entities.CartItem;
import com.service.cart.requests.CartItemRequest;
import com.service.cart.resources.CartItemProdResource;
import com.service.cart.resources.CartItemResource;

public interface CartItemInterface {

    CartItem createCartItem(CartItemRequest request);

    CartItem updateCartItem(CartItemRequest request);

    Boolean deleteCartItem(Integer id);

    List<CartItemResource> getCartItemByUserId(UUID userId);

    List<CartItemProdResource> getCartItemProdByUserId(String userId);
}
