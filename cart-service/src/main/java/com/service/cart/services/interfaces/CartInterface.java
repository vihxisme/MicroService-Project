package com.service.cart.services.interfaces;

import com.service.cart.entities.Cart;
import com.service.cart.requests.CartRequest;

public interface CartInterface {

    Cart createCart(CartRequest request);
}
