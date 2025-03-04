package com.service.cart.mappers;

import java.util.UUID;

import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.service.cart.entities.Cart;
import com.service.cart.repositories.CartRepository;

import jakarta.persistence.EntityNotFoundException;

@Component
public class Convert {

    @Autowired
    private CartRepository cartRepository;

    @Named("uuidToCart")
    public Cart uuidToCart(UUID cartId) {
        return cartRepository.findById(cartId).orElseThrow(()
                -> new EntityNotFoundException("Cart not found"));
    }
}
