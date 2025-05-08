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

import com.service.apicomposition.mappers.OrderClientMapper;
import com.service.apicomposition.resources.OrderItemProdResource;
import com.service.apicomposition.resources.OrderItemResource;
import com.service.apicomposition.resources.ProdIdNameResource;
import com.service.apicomposition.resources.ProdVariantColorSizeResource;

import reactor.core.publisher.Mono;

@Service
public class OrderClientService {

    @Autowired
    @Qualifier("orderWebClient")
    private WebClient orderClient;

    @Autowired
    @Qualifier("productWebClient")
    private WebClient productClient;

    @Autowired
    private OrderClientMapper orderClientMapper;

    private Logger logger = LoggerFactory.getLogger(OrderClientService.class);

    private Mono<List<OrderItemResource>> orderItemListMono(String path, UUID orderId) {
        return orderClient.get()
                .uri(uriBuilder -> uriBuilder
                .path(path)
                .queryParam("orderId", orderId)
                .build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<OrderItemResource>>() {
                });

    }

    private Mono<Map<UUID, ProdIdNameResource>> prodIdNameMono(List<OrderItemResource> orderItemsList, String path) {
        return orderItemsList.isEmpty() ? Mono.just(Collections.emptyMap())
                : productClient.get()
                        .uri(uriBuilder -> uriBuilder.path(path)
                        .queryParam("productIds", orderItemsList.stream()
                                .map(OrderItemResource::getProductId)
                                .collect(Collectors.toList()))
                        .build())
                        .retrieve()
                        .bodyToMono(new ParameterizedTypeReference<Map<UUID, ProdIdNameResource>>() {
                        });
    }

    private Mono<Map<Integer, ProdVariantColorSizeResource>> prodVariColorSizeMono(List<OrderItemResource> orderItemsList, String path) {
        return orderItemsList.isEmpty() ? Mono.just(Collections.emptyMap())
                : productClient.get()
                        .uri(uriBuilder -> uriBuilder.path(path)
                        .queryParam("variantIds", orderItemsList.stream()
                                .map(OrderItemResource::getProdVariantId)
                                .collect(Collectors.toList()))
                        .build())
                        .retrieve()
                        .bodyToMono(new ParameterizedTypeReference<Map<Integer, ProdVariantColorSizeResource>>() {
                        });
    }

    private Mono<List<OrderItemProdResource>> fetchOrderItemProd(
            Mono<List<OrderItemResource>> orderItemList
    ) {
        return orderItemList.flatMap(orderItems -> {
            Mono<Map<UUID, ProdIdNameResource>> prodIdNameMono = prodIdNameMono(orderItems, "/internal/products/id-name/by");
            Mono<Map<Integer, ProdVariantColorSizeResource>> prodVariantColorSizeMono = prodVariColorSizeMono(orderItems, "/internal/product-variant/color-size/by");
            return Mono.zip(prodIdNameMono, prodVariantColorSizeMono)
                    .map(tuple -> {
                        Map<UUID, ProdIdNameResource> prodIdNames = tuple.getT1();
                        Map<Integer, ProdVariantColorSizeResource> prodVariantColorSizes = tuple.getT2();

                        return orderItems.stream()
                                .map(orderItem -> {
                                    ProdIdNameResource prod = prodIdNames.get(orderItem.getProductId());
                                    ProdVariantColorSizeResource variant = prodVariantColorSizes.get(orderItem.getProdVariantId());

                                    return orderClientMapper.toOrderItemProdResource(orderItem, prod, variant);
                                })
                                .collect(Collectors.toList());
                    })
                    .doOnSuccess(result -> logger.info("Result: {}", result))
                    .doOnError(error -> logger.error("Error: ", error));

        });
    }

    public Mono<List<OrderItemProdResource>> getOrderItemProd(UUID orderId) {
        return fetchOrderItemProd(
                orderItemListMono(
                        "/internal/order-item/by",
                        orderId
                )
        );
    }
}
