package com.service.cart.services.impl;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.service.cart.entities.Cart;
import com.service.cart.mappers.CartMapper;
import com.service.cart.repositories.CartRepository;
import com.service.cart.requests.CartRequest;
import com.service.cart.services.interfaces.CartInterface;

@Service
public class CartService implements CartInterface {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartMapper cartMapper;

    private String generateCartCode() {
        Boolean isUnique;
        Random random = new Random();
        String code;
        do {
            int randomNumberStart = random.nextInt(100, 999);
            int randomNumberMiddle = random.nextInt(10000, 99999);
            int randomNumberEnd = random.nextInt(10000, 99999);
            code = String.format("%03d-%05d-%05d", randomNumberStart, randomNumberMiddle, randomNumberEnd);
            isUnique = !cartRepository.existsByCartCode(code);
        } while (!isUnique);

        return code;
    }

    @Override
    public Cart createCart(CartRequest request) {
        if (request.getCartCode() == null) {
            request.setCartCode(generateCartCode());
        }

        Cart cart = cartMapper.toCart(request);

        return cartRepository.save(cart);
    }
}
