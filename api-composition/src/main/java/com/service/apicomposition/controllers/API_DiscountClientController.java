package com.service.apicomposition.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;

import com.service.apicomposition.requests.PaginationRequest;
import com.service.apicomposition.services.DiscountClientService;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/discount-client")
public class API_DiscountClientController {

    @Autowired
    private DiscountClientService discountClientService;

    private Logger logger = LoggerFactory.getLogger(API_DiscountClientController.class);

    @GetMapping("/target/**")
    public Mono<ResponseEntity<Object>> getProductsWithDiscount(ServerWebExchange exchange) {
        // Lấy thông tin phân trang từ query parameters
        int page = Integer.parseInt(exchange.getRequest().getQueryParams().getFirst("page"));
        int size = Integer.parseInt(exchange.getRequest().getQueryParams().getFirst("size"));
        PaginationRequest request = PaginationRequest.builder().page(page).size(size).build();

        return discountClientService.getDiscountTargetWithProductClient(request)
                .map(productWithDiscountList -> ResponseEntity.ok((Object) productWithDiscountList))
                .onErrorResume(ex -> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error fetching products with discount")));
    }

}
