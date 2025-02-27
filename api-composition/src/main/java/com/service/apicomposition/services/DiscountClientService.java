package com.service.apicomposition.services;

import java.time.Duration;
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
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.service.apicomposition.enums.TargetTypeEnum;
import com.service.apicomposition.mappers.DiscountClientMapper;
import com.service.apicomposition.requests.PaginationRequest;
import com.service.apicomposition.resources.DiscountWithTarget;
import com.service.apicomposition.resources.DiscountWithTargetNameResource;
import com.service.apicomposition.responses.PaginationResponse;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class DiscountClientService {

    @Autowired
    @Qualifier("discountWebClient")
    private WebClient discountClient;

    @Autowired
    @Qualifier("productWebClient")
    private WebClient productClient;

    @Autowired
    private DiscountClientMapper discountClientMapper;

    @Autowired
    private ReactiveRedisTemplate<String, Object> reactiveRedisTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    private Logger logger = LoggerFactory.getLogger(DiscountClientService.class);

    // lấy danh sách discount-target từ discount-service với {size} bản ghi
    private Mono<PaginationResponse<DiscountWithTarget>> fetchDiscountListMono(String path, int page, int size) {
        return discountClient.get()
                .uri(uriBuilder -> uriBuilder
                .path(path)
                .queryParam("page", page)
                .queryParam("size", size)
                .build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<PaginationResponse<DiscountWithTarget>>() {
                });
    }

    // Request lấy tên sản phẩm từ targetId
    private Mono<Map<UUID, String>> productNamesMono(List<DiscountWithTarget> discountList, String path) {
        return discountList.isEmpty() ? Mono.just(Collections.emptyMap())
                : productClient.get()
                        .uri(uriBuilder -> uriBuilder.path(path)
                        .queryParam("productIds", discountList.stream()
                                .filter(d -> TargetTypeEnum.PRODUCT.toString().equals(d.getTargetType()))
                                .map(DiscountWithTarget::getTargetId)
                                .collect(Collectors.toList()))
                        .build())
                        .retrieve()
                        .bodyToMono(new ParameterizedTypeReference<Map<UUID, String>>() {
                        });
    }

    // Request lấy tên danh mục từ targetId
    private Mono<Map<UUID, String>> categoryNamesMono(List<DiscountWithTarget> discountList, String path) {
        return discountList.isEmpty() ? Mono.just(Collections.emptyMap())
                : productClient.get()
                        .uri(uriBuilder -> uriBuilder.path(path)
                        .queryParam("categorieIds", discountList.stream()
                                .filter(d -> TargetTypeEnum.CATEGORIE.toString().equals(d.getTargetType()))
                                .map(DiscountWithTarget::getTargetId)
                                .collect(Collectors.toList()))
                        .build())
                        .retrieve()
                        .bodyToMono(new ParameterizedTypeReference<Map<UUID, String>>() {
                        });
    }

    private Mono<PaginationResponse<DiscountWithTargetNameResource>> fetchDiscountTargetWithProductClient(
            Mono<PaginationResponse<DiscountWithTarget>> discountListMono) {

        return discountListMono.flatMap(discountPage -> {
            // Lấy danh sách phân trang
            List<DiscountWithTarget> discountList = discountPage.getContent();

            // Tạo request lấy tên sản phẩm và danh mục
            Mono<Map<UUID, String>> productNamesMono = productNamesMono(discountList, "/internal/product-names");
            Mono<Map<UUID, String>> categoryNamesMono = categoryNamesMono(discountList, "/internal/categorie-names");

            return Mono.zip(productNamesMono, categoryNamesMono)
                    .map(tuple -> {
                        Map<UUID, String> productNames = tuple.getT1();
                        Map<UUID, String> categoryNames = tuple.getT2();

                        List<DiscountWithTargetNameResource> discountWithTargetNames = discountList.stream()
                                .map(discount -> {
                                    String targetName = "Unknown";
                                    if (TargetTypeEnum.PRODUCT.toString().equals(discount.getTargetType())) {
                                        if (discount.getTargetId() != null) {
                                            targetName = productNames.getOrDefault(discount.getTargetId(), "Unknown Product");
                                        } else {
                                            targetName = "Toàn bộ sản phẩm";
                                        }
                                    } else if (TargetTypeEnum.CATEGORIE.toString().equals(discount.getTargetType())) {
                                        targetName = categoryNames.getOrDefault(discount.getTargetId(), "Unknown Category");
                                    }
                                    return discountClientMapper.toDiscountWithTargetNameResource(discount, targetName);
                                })
                                .collect(Collectors.toList());

                        return PaginationResponse.<DiscountWithTargetNameResource>builder()
                                .content(discountWithTargetNames)
                                .pageNumber(discountPage.getPageNumber())
                                .pageSize(discountPage.getPageSize())
                                .totalPages(discountPage.getTotalPages())
                                .totalElements(discountPage.getTotalElements())
                                .build();
                    })
                    .doOnSuccess(result -> logger.info("Result: {}", result))
                    .doOnError(error -> logger.error("Error: ", error));
        });
    }

    public Mono<PaginationResponse<DiscountWithTargetNameResource>> getDiscountTargetWithProductClient(PaginationRequest request) {
        String cacheKey = String.format("DiscountTarget_%d_%d", request.getPage(), request.getSize());
        final long DURATION_TTL = 3600;

        logger.error("cacheKey: " + cacheKey);
        // Lấy dữ liệu từ Redis
        return reactiveRedisTemplate.opsForValue().get(cacheKey)
                .doOnError(error -> logger.error("Error fetching from Redis: ", error))
                .map(json -> {
                    logger.info("JSON from Redis: {}", json);
                    return objectMapper.convertValue(json, new TypeReference<PaginationResponse<DiscountWithTargetNameResource>>() {
                    });
                })
                // Nếu không có dữ liệu trên Redis thì sẽ gọi API để tổng hợp dữ liệu và đẩy lên Redis
                .switchIfEmpty(fetchDiscountTargetWithProductClient(fetchDiscountListMono("/internal/discount-client/with-target", request.getPage(), request.getSize()))
                        .doOnNext(data -> logger.info("Data from API: {}", data))
                        .flatMap(data -> reactiveRedisTemplate.opsForValue()
                        .set(cacheKey, data, Duration.ofSeconds(DURATION_TTL))
                        .thenReturn(data)));
    }

}
