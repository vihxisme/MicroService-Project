package com.service.apicomposition.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;

import com.service.apicomposition.requests.PaginationRequest;
import com.service.apicomposition.services.ProductClientService;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/product-client")
public class API_ProductClientController {

    @Autowired
    private ProductClientService productClientService;

    @GetMapping("/with-discount/**")
    public Mono<ResponseEntity<Object>> getProductsWithDiscount(ServerWebExchange exchange) {

        int page = Integer.parseInt(exchange.getRequest().getQueryParams().getFirst("page"));
        int size = Integer.parseInt(exchange.getRequest().getQueryParams().getFirst("size"));
        PaginationRequest request = PaginationRequest.builder().page(page).size(size).build();

        return productClientService.getProductWithDiscount(request)
                .map(productWithDiscountList -> ResponseEntity.ok((Object) productWithDiscountList))
                .onErrorResume(ex -> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Network Error")));
    }

    @GetMapping("/by-categorie")
    public Mono<ResponseEntity<Object>> getProductsWithDiscountByCategorie(ServerWebExchange exchange) {

        String categorieId = exchange.getRequest().getQueryParams().getFirst("categorieId");
        int page = Integer.parseInt(exchange.getRequest().getQueryParams().getFirst("page"));
        int size = Integer.parseInt(exchange.getRequest().getQueryParams().getFirst("size"));
        PaginationRequest request = PaginationRequest.builder().page(page).size(size).build();

        return productClientService.getProductWithDiscountByCategorie(categorieId, request)
                .map(data -> ResponseEntity.ok((Object) data))
                .onErrorResume(ex -> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Network Error")));
    }

    @GetMapping("/only-discount")
    public Mono<ResponseEntity<Object>> getOnlyProductDiscount(ServerWebExchange exchange) {
        int page = Integer.parseInt(exchange.getRequest().getQueryParams().getFirst("page"));
        int size = Integer.parseInt(exchange.getRequest().getQueryParams().getFirst("size"));
        PaginationRequest request = PaginationRequest.builder().page(page).size(size).build();

        return productClientService.getOnlyProductWithDiscount(request)
                .map(data -> ResponseEntity.ok((Object) data))
                .onErrorResume(ex -> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Network Error")));
    }

}
