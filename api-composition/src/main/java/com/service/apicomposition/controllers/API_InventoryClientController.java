package com.service.apicomposition.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;

import com.service.apicomposition.requests.PaginationRequest;
import com.service.apicomposition.services.InventoryClientService;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/inventory-client")
public class API_InventoryClientController {

    @Autowired
    private InventoryClientService inventoryClientService;

    @GetMapping("/inventory/with-product")
    public Mono<ResponseEntity<Object>> getInventoryWithProduct(ServerWebExchange exchange) {
        // Lấy thông tin phân trang từ query parameters
        int page = Integer.parseInt(exchange.getRequest().getQueryParams().getFirst("page"));
        int size = Integer.parseInt(exchange.getRequest().getQueryParams().getFirst("size"));
        PaginationRequest request = PaginationRequest.builder().page(page).size(size).build();

        return inventoryClientService.getInventoryWithProduct(request)
                .map(inventoryProduct -> ResponseEntity.ok((Object) inventoryProduct))
                .onErrorResume(ex -> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Network Error")));
    }

    @GetMapping("/inventory-items")
    public Mono<ResponseEntity<Object>> getItemProdVariant(ServerWebExchange exchange) {
        String inventoryId = exchange.getRequest().getQueryParams().getFirst("inventoryId");

        // Lấy thông tin phân trang từ query parameters
        int page = Integer.parseInt(exchange.getRequest().getQueryParams().getFirst("page"));
        int size = Integer.parseInt(exchange.getRequest().getQueryParams().getFirst("size"));
        PaginationRequest request = PaginationRequest.builder().page(page).size(size).build();

        return inventoryClientService.getItemProdVariant(inventoryId, request)
                .map(inventoryProduct -> ResponseEntity.ok((Object) inventoryProduct))
                .onErrorResume(ex -> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Network Error")));
    }

    @GetMapping("/stock-movement/type-in")
    public Mono<ResponseEntity<Object>> getStockMvmInProd(ServerWebExchange exchange) {
        // Lấy thông tin phân trang từ query parameters
        int page = Integer.parseInt(exchange.getRequest().getQueryParams().getFirst("page"));
        int size = Integer.parseInt(exchange.getRequest().getQueryParams().getFirst("size"));
        PaginationRequest request = PaginationRequest.builder().page(page).size(size).build();

        return inventoryClientService.getStockMvmInProd(request)
                .map(inventoryProduct -> ResponseEntity.ok((Object) inventoryProduct))
                .onErrorResume(ex -> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Network Error")));
    }

    @GetMapping("/stock-movement/type-out")
    public Mono<ResponseEntity<Object>> getStockMvmOutProd(ServerWebExchange exchange) {
        // Lấy thông tin phân trang từ query parameters
        int page = Integer.parseInt(exchange.getRequest().getQueryParams().getFirst("page"));
        int size = Integer.parseInt(exchange.getRequest().getQueryParams().getFirst("size"));
        PaginationRequest request = PaginationRequest.builder().page(page).size(size).build();

        return inventoryClientService.getStockMvmOutProd(request)
                .map(inventoryProduct -> ResponseEntity.ok((Object) inventoryProduct))
                .onErrorResume(ex -> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Network Error")));
    }
}
