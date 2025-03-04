package com.service.cart.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.service.cart.requests.CartRequest;
import com.service.cart.responses.SuccessResponse;
import com.service.cart.services.interfaces.CartInterface;

@RestController
@RequestMapping("/v1/cart")
public class CartController {

    @Autowired
    private CartInterface cartInterface;

    public ResponseEntity<?> createCart(@RequestBody CartRequest request) {
        return ResponseEntity.ok(
                new SuccessResponse<>(
                        "SUCCESS",
                        cartInterface.createCart(request))
        );
    }
}
