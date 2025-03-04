package com.service.cart.repositories;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "api-composition")
public interface ApiClient {

    @GetMapping("/cart-client/cart-item")
    ResponseEntity<?> getCartItemProd(@RequestParam String userId);
}
