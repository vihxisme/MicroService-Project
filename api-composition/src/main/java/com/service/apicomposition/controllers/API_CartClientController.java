package com.service.apicomposition.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;

import com.service.apicomposition.services.CartClientService;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/cart-client")
public class API_CartClientController {

    @Autowired
    private CartClientService cartClientService;

    @GetMapping("/with-prod")
    public Mono<ResponseEntity<Object>> getCartItemProd(ServerWebExchange exchange) {
        String userId = exchange.getRequest().getQueryParams().getFirst("userId");

        return cartClientService.getCartItemProdMono(userId)
                .map(cartClientProd -> ResponseEntity.ok((Object) cartClientProd))
                .onErrorResume(ex -> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Network Error" + ex)));
    }
}
