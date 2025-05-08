package com.service.apicomposition.controllers;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;

import com.service.apicomposition.services.OrderClientService;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/order-client")
public class API_OrderClientController {

    @Autowired
    private OrderClientService orderClientService;

    @GetMapping("/order-item/detail")
    public Mono<ResponseEntity<Object>> getCustomerTransaction(ServerWebExchange exchange) {
        // Lấy thông tin phân trang từ query parameters
        UUID orderId = UUID.fromString(exchange.getRequest().getQueryParams().getFirst("orderId"));

        return orderClientService.getOrderItemProd(orderId)
                .map(orderItem -> ResponseEntity.ok((Object) orderItem))
                .onErrorResume(ex -> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Network Error")));
    }
}
