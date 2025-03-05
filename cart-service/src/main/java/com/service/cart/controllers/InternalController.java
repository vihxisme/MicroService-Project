package com.service.cart.controllers;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.service.cart.services.interfaces.CartItemInterface;

@RestController
@RequestMapping("/internal")
public class InternalController {

    @Autowired
    private CartItemInterface cartItemInterface;

    @GetMapping("/cart-item")
    public ResponseEntity<?> getCartItemByuserId(@RequestParam String userId) {
        return ResponseEntity.ok(cartItemInterface.getCartItemByUserId(UUID.fromString(userId)));
    }
}
