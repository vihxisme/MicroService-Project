package com.service.cart.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.service.cart.requests.CartItemRequest;
import com.service.cart.responses.SuccessResponse;
import com.service.cart.services.interfaces.CartItemInterface;

@RestController
@RequestMapping("/v1/cart-item")
public class CartItemController {

    @Autowired
    private CartItemInterface cartItemInterface;

    @PostMapping("/create")
    public ResponseEntity<?> createCartItem(@RequestBody CartItemRequest request) {
        return ResponseEntity.ok(
                new SuccessResponse<>(
                        "SUCCESS",
                        cartItemInterface.createCartItem(request))
        );
    }

    @PatchMapping("/update")
    public ResponseEntity<?> updateCartItem(@RequestBody CartItemRequest request) {
        return ResponseEntity.ok(
                new SuccessResponse<>(
                        "SUCCESS",
                        cartItemInterface.updateCartItem(request))
        );
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteCartItem(@PathVariable Integer id) {
        return ResponseEntity.ok(
                new SuccessResponse<>(
                        "SUCCESS",
                        cartItemInterface.deleteCartItem(id))
        );
    }

    @GetMapping("/with-prod")
    public ResponseEntity<?> getCartItemProd(@RequestParam String userId) {
        return ResponseEntity.ok(
                new SuccessResponse<>(
                        "SUCCESS",
                        cartItemInterface.getCartItemProdByUserId(userId))
        );
    }
}
