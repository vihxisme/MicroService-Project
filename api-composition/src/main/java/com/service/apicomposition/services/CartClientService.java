package com.service.apicomposition.services;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.service.apicomposition.mappers.CartMapper;
import com.service.apicomposition.resources.CartItemProdResource;
import com.service.apicomposition.resources.CartItemResource;
import com.service.apicomposition.resources.ProdAndStatusResource;
import com.service.apicomposition.resources.ProdVariantResource;

import reactor.core.publisher.Mono;

@Service
public class CartClientService {

    @Autowired
    @Qualifier("cartWebClient")
    private WebClient cartClient;

    @Autowired
    @Qualifier("productWebClient")
    private WebClient productClient;

    @Autowired
    private CartMapper cartMapper;

    @Autowired
    private RedisService redisService;

    private Logger logger = LoggerFactory.getLogger(CartClientService.class);

    private final long DURATION_TTL = 3600;

    // lấy danh sách CartItem từ cart-service
    private Mono<List<CartItemResource>> fetchCartItemListMono(String userId, String path) {
        return cartClient.get()
                .uri(uriBuilder -> uriBuilder
                .path(path)
                .queryParam("userId", userId)
                .build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<CartItemResource>>() {
                })
                .doOnNext(prodAndStatusList -> logger.info("cart item s: {}", prodAndStatusList))
                .doOnError(error -> logger.error("cart item e: ", error));
    }

    // lấy danh sách ProdVariantResource từ CartItemResource.getProdVariantId
    private Mono<List<ProdVariantResource>> fetchProdVariantListMono(List<CartItemResource> cartItemList, String path) {
        return cartItemList.isEmpty() ? Mono.just(Collections.emptyList())
                : productClient.get()
                        .uri(uriBuilder -> uriBuilder.path(path)
                        .queryParam("variantIds", cartItemList.stream()
                                .map(CartItemResource::getProdVariantId)
                                .collect(Collectors.toList()))
                        .build())
                        .retrieve()
                        .bodyToMono(new ParameterizedTypeReference<List<ProdVariantResource>>() {
                        })
                        .doOnNext(prodAndStatusList -> logger.info("Prod variant s: {}", prodAndStatusList))
                        .doOnError(error -> logger.error("Prod variant e: ", error));
    }

    // lấy danh sách ProdAndStatusResource từ CartItemResource.getProductId()
    private Mono<List<ProdAndStatusResource>> fetchProdAndStatusListMono(List<CartItemResource> cartItemList, String path) {
        return cartItemList.isEmpty() ? Mono.just(Collections.emptyList())
                : productClient.get()
                        .uri(uriBuilder -> uriBuilder
                        .path(path)
                        .queryParam("productIds", cartItemList.stream()
                                .map(CartItemResource::getProductId)
                                .collect(Collectors.toList()))
                        .build())
                        .retrieve()
                        .bodyToMono(new ParameterizedTypeReference<List<ProdAndStatusResource>>() {
                        })
                        .doOnNext(prodAndStatusList -> logger.info("Prod and status s: {}", prodAndStatusList))
                        .doOnError(error -> logger.error("Prod and status e: ", error));
    }

    // tổng hợp dữ liệu tự fetchProdVariantListMono, fetchProdAndStatusListMono và fetchCartItemListMono
    private Mono<List<CartItemProdResource>> fetchCartItemProdMono(
            Mono<List<CartItemResource>> cartItemListMono
    ) {
        return cartItemListMono.flatMap(cartItemList -> {
            Mono<List<ProdVariantResource>> prodVariantMono = fetchProdVariantListMono(cartItemList, "/internal/prod-variant");
            Mono<List<ProdAndStatusResource>> prodAndStatusMono = fetchProdAndStatusListMono(cartItemList, "/internal/product-status");

            return Mono.zip(prodVariantMono, prodAndStatusMono)
                    .map(tuple -> {
                        List<ProdVariantResource> prodVariantList = tuple.getT1();
                        List<ProdAndStatusResource> prodAndStatusList = tuple.getT2();

                        // Tạo map để tìm kiếm nhanh
                        Map<Integer, ProdVariantResource> variantMap = prodVariantList.stream()
                                .collect(Collectors.toMap(ProdVariantResource::getId, v -> v));

                        Map<UUID, ProdAndStatusResource> statusMap = prodAndStatusList.stream()
                                .collect(Collectors.toMap(ProdAndStatusResource::getProductId, s -> s));

                        return cartItemList.stream()
                                .map(cartItem -> {
                                    ProdVariantResource variant = variantMap.get(cartItem.getProdVariantId());
                                    ProdAndStatusResource productStatus = statusMap.get(cartItem.getProductId());

                                    return cartMapper.toCartItemProdResource(cartItem, variant, productStatus);
                                })
                                .collect(Collectors.toList());
                    })
                    .doOnSuccess(result -> logger.info("Result: {}", result))
                    .doOnError(error -> logger.error("Error: ", error));
        });
    }

    // lấy dữ liệu fetchCartItemProdMono từ redis, không có trong redis thì call api rồi lưu vào redis
    public Mono<List<CartItemProdResource>> getCartItemProdMono(String userId) {
        String cacheKey = String.format("cart:item:prod:%s", userId);
        logger.info("cacheKey: " + cacheKey);

        return redisService.getData(cacheKey, new ParameterizedTypeReference<List<CartItemProdResource>>() {
        })
                .switchIfEmpty(fetchCartItemProdMono(
                        fetchCartItemListMono(userId, "/internal/cart-item")
                ))
                .flatMap(data -> redisService.saveData(cacheKey, data, DURATION_TTL).thenReturn(data));
        // return redisService.getData(cacheKey, new ParameterizedTypeReference<List<CartItemProdResource>>() {
        // })
        //         .doOnSubscribe(subscription -> logger.info("Fetching data from cache with key: " + cacheKey))
        //         .doOnNext(data -> logger.info("Data fetched from cache: " + data))
        //         .switchIfEmpty(
        //                 fetchCartItemProdMono(
        //                         fetchCartItemListMono(userId, "/internal/cart-item")
        //                 )
        //                         .doOnSubscribe(subscription -> logger.info("Fetching data from external service for user: " + userId))
        //                         .doOnNext(data -> logger.info("Data fetched from external service: " + data))
        //         )
        //         .flatMap(data
        //                 -> redisService.saveData(cacheKey, data, DURATION_TTL)
        //                 .doOnSubscribe(subscription -> logger.info("Saving data to cache with key: " + cacheKey))
        //                 .doOnSuccess(unused -> logger.info("Data saved to cache successfully"))
        //                 .thenReturn(data)
        //         );
    }

}
