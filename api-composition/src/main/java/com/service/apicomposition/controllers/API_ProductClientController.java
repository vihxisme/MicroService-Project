package com.service.apicomposition.controllers;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.server.ServerWebExchange;

import com.service.apicomposition.helper.WebClientExceptionHandler;
import com.service.apicomposition.requests.PaginationRequest;
import com.service.apicomposition.services.ProductClientService;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/product-client")
public class API_ProductClientController {

    @Autowired
    private ProductClientService productClientService;

    @Autowired
    @Qualifier("productWebClient")
    private WebClient productClient;

    @Autowired
    private WebClientExceptionHandler webClientExceptionHandler;

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

    @GetMapping("/apparel-type")
    public Mono<ResponseEntity<Object>> getOnlyProdWithDiscountByCateApparelType(ServerWebExchange exchange) {
        Integer apparelType = Integer.valueOf(exchange.getRequest().getQueryParams().getFirst("apparelType"));
        int page = Integer.parseInt(exchange.getRequest().getQueryParams().getFirst("page"));
        int size = Integer.parseInt(exchange.getRequest().getQueryParams().getFirst("size"));
        PaginationRequest request = PaginationRequest.builder().page(page).size(size).build();

        return productClientService.getProdWithDiscountWithByCateApparelType(apparelType, request)
                .map(data -> ResponseEntity.ok((Object) data))
                .onErrorResume(ex -> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Network Error")));
    }

    @GetMapping("/detail-info")
    public Mono<ResponseEntity<Object>> getProdWithDiscountAllInfoById(ServerWebExchange exchange) {
        UUID productId = UUID.fromString(exchange.getRequest().getQueryParams().getFirst("id"));

        return productClientService.getProdWithDiscountAllInfoById(productId)
                .map(data -> ResponseEntity.ok((Object) data))
                .onErrorResume(ex -> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Network Error")));
    }

    @GetMapping("/top-product/by-revenue")
    public Mono<ResponseEntity<Object>> getTopProductRevenue(ServerWebExchange exchange) {
        Integer limit = Integer.valueOf(exchange.getRequest().getQueryParams().getFirst("limit"));

        return productClientService.getListTopProduct(limit)
                .map(data -> ResponseEntity.ok((Object) data))
                .onErrorResume(ex -> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Network Error")));
    }

    @GetMapping("/top-product/with-discount")
    public Mono<ResponseEntity<Object>> getTopProductWithDiscount(ServerWebExchange exchange) {
        Integer limit = Integer.valueOf(exchange.getRequest().getQueryParams().getFirst("limit"));

        return productClientService.getTopProductWithDiscount(limit)
                .map(data -> ResponseEntity.ok((Object) data))
                .onErrorResume(ex -> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Network Error")));
    }

    @GetMapping("/top-product/by-rangetype")
    public Mono<ResponseEntity<Object>> getTopProductByRangeType(ServerWebExchange exchange) {
        Integer limit = Integer.valueOf(exchange.getRequest().getQueryParams().getFirst("limit"));
        String rangeType = exchange.getRequest().getQueryParams().getFirst("rangeType");

        return productClientService.getListTopProduct(limit, rangeType)
                .map(data -> ResponseEntity.ok((Object) data))
                .onErrorResume(ex -> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Network Error")));
    }

    @PostMapping("prod-send-email")
    public Mono<ResponseEntity<Object>> getProdIdNameByProductId(ServerWebExchange exchange) {
        ServerHttpRequest request = exchange.getRequest();

        String path = "/internal/prod-send-email";
        HttpMethod method = request.getMethod();

        WebClient.RequestHeadersSpec<?> requestSpec = productClient.method(method).uri(path);

        return exchange.getRequest().getBody()
                .collectList()
                .flatMap(body -> {
                    if (body.isEmpty()) {
                        return ((WebClient.RequestBodySpec) requestSpec)
                                .contentType(MediaType.APPLICATION_JSON)
                                .retrieve()
                                .bodyToMono(Object.class);
                    }
                    return ((WebClient.RequestBodySpec) requestSpec)
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(BodyInserters.fromDataBuffers(Flux.fromIterable(body)))
                            .retrieve()
                            .bodyToMono(Object.class);
                })
                .map(ResponseEntity::ok)
                .onErrorResume(WebClientResponseException.class, webClientExceptionHandler::handleWebClientException);
    }
}
