package com.service.apicomposition.services;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.service.apicomposition.enums.TargetTypeEnum;
import com.service.apicomposition.mappers.ComponentMapper;
import com.service.apicomposition.mappers.ProductDiscountMapper;
import com.service.apicomposition.requests.PaginationRequest;
import com.service.apicomposition.resources.DiscountClientResource;
import com.service.apicomposition.resources.ProductClientResource;
import com.service.apicomposition.resources.ProdWithDiscountResource;
import com.service.apicomposition.responses.PaginationResponse;

import reactor.core.publisher.Mono;

@Service
public class ProductClientService {

    @Autowired
    @Qualifier("productWebClient")
    private WebClient productClient;

    @Autowired
    @Qualifier("discountWebClient")
    private WebClient discountClient;

    @Autowired
    private RedisService redisService;

    @Autowired
    private ComponentMapper componentMapper;

    @Autowired
    private ProductDiscountMapper productDiscountMapper;

    private Logger logger = LoggerFactory.getLogger(ProductClientService.class);

    // lấy danh sách sản phẩm từ product-service với {size} bản ghi
    private Mono<PaginationResponse<ProductClientResource>> fetchProductPageMono(String path, int page, int size) {
        return productClient.get()
                .uri(uriBuilder -> uriBuilder
                .path(path)
                .queryParam("page", page)
                .queryParam("size", size)
                .build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<PaginationResponse<ProductClientResource>>() {
                });
    }

    private Mono<PaginationResponse<ProductClientResource>> fetchProductPageMonoByCategorie(String path, String categorieId, int page, int size) {
        return productClient.get()
                .uri(uriBuilder -> uriBuilder
                .path(path)
                .queryParam("categorieId", categorieId)
                .queryParam("page", page)
                .queryParam("size", size)
                .build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<PaginationResponse<ProductClientResource>>() {
                });
    }

    // lấy danh sách khuyến mãi từ discount-service
    private Mono<List<DiscountClientResource>> fetchDiscountListMono(String path) {
        return discountClient.get()
                .uri(path)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<DiscountClientResource>>() {
                });
    }

    // tổng hợp dữ liệu lấy được từ product-service và discount-service
    private Mono<PaginationResponse<ProdWithDiscountResource>> fetchProductsWithDiscount(
            Mono<PaginationResponse<ProductClientResource>> productPageMono,
            Mono<List<DiscountClientResource>> discountListMono) {

        return Mono.zip(productPageMono, discountListMono)
                .map(tuple -> {
                    PaginationResponse<ProductClientResource> productPage = tuple.getT1();
                    List<DiscountClientResource> discountList = tuple.getT2();

                    // Lọc giảm giá phù hợp với từng sản phẩm
                    List<ProdWithDiscountResource> combinedList = productPage.getContent()
                            .stream()
                            .map(product -> {
                                DiscountClientResource discount = discountList.stream()
                                        .filter(d -> (d.getTargetId() == null
                                        && TargetTypeEnum.PRODUCT
                                                .toString()
                                                .equals(d.getTargetType()))
                                        || (TargetTypeEnum.PRODUCT.toString().equals(d.getTargetType())
                                        && Objects.equals(
                                                d.getTargetId(),
                                                product.getId()))
                                        || (TargetTypeEnum.CATEGORIE.toString().equals(d.getTargetType())
                                        && Objects.equals(
                                                d.getTargetId(),
                                                product.getCategorieId())))
                                        .findFirst()
                                        .orElse(new DiscountClientResource(
                                                product.getId(),
                                                BigDecimal.ZERO, null,
                                                null,
                                                null, null));

                                return productDiscountMapper
                                        .toProductWithDiscountResource(product,
                                                discount,
                                                componentMapper);
                            })
                            .toList();

                    // Tạo PaginationResponse mới giữ nguyên thông tin phân trang
                    return PaginationResponse.<ProdWithDiscountResource>builder()
                            .content(combinedList)
                            .pageNumber(productPage.getPageNumber())
                            .pageSize(productPage.getPageSize())
                            .totalPages(productPage.getTotalPages())
                            .totalElements(productPage.getTotalElements())
                            .build();
                })
                .doOnSuccess(result -> logger.info("Result: {}", result))
                .doOnError(error -> logger.error("Error: ", error));

    }

    // tổng hợp dữ liệu lấy được từ product-service và discount-service (chỉ lấy những sản phẩm khuyến mãi)
    private Mono<PaginationResponse<ProdWithDiscountResource>> fetchProductandDiscount(
            Mono<PaginationResponse<ProductClientResource>> productPageMono,
            Mono<List<DiscountClientResource>> discountListMono) {

        return Mono.zip(productPageMono, discountListMono)
                .map(tuple -> {
                    PaginationResponse<ProductClientResource> productPage = tuple.getT1();
                    List<DiscountClientResource> discountList = tuple.getT2();

                    // Lọc giảm giá phù hợp với từng sản phẩm
                    List<ProdWithDiscountResource> combinedList = productPage.getContent()
                            .stream()
                            .map(product -> {
                                DiscountClientResource discount = discountList.stream()
                                        .filter(d -> (d.getTargetId() == null
                                        && TargetTypeEnum.PRODUCT
                                                .toString()
                                                .equals(d.getTargetType()))
                                        || (TargetTypeEnum.PRODUCT.toString().equals(d.getTargetType())
                                        && Objects.equals(d.getTargetId(), product.getId()))
                                        || (TargetTypeEnum.CATEGORIE.toString().equals(d.getTargetType())
                                        && Objects.equals(d.getTargetId(), product.getCategorieId())))
                                        .findFirst()
                                        .orElse(null);

                                return discount != null ? productDiscountMapper.toProductWithDiscountResource(product, discount, componentMapper) : null;
                            })
                            .filter(Objects::nonNull)
                            .toList();

                    // Tạo PaginationResponse mới giữ nguyên thông tin phân trang nhưng cập nhật số lượng phần tử
                    return PaginationResponse.<ProdWithDiscountResource>builder()
                            .content(combinedList)
                            .pageNumber(productPage.getPageNumber())
                            .pageSize(productPage.getPageSize())
                            .totalPages(productPage.getTotalPages())
                            .totalElements(combinedList.size())
                            .build();
                })
                .doOnSuccess(result -> logger.info("Result: {}", result))
                .doOnError(error -> logger.error("Error: ", error));
    }

    // lấy ra danh sách product từ redis, nếu không có trong redis sẽ call api để tổng hợp dữ liệu để đẩy lên redis
    public Mono<PaginationResponse<ProdWithDiscountResource>> getProductWithDiscount(PaginationRequest request) {
        String cacheKey = String.format("prod:discount:%d:%d", request.getPage(), request.getSize());
        final long DURATION_TTL = 3600;

        return redisService.getData(cacheKey, new ParameterizedTypeReference<PaginationResponse<ProdWithDiscountResource>>() {
        })
                .switchIfEmpty(fetchProductsWithDiscount(
                        fetchProductPageMono("/internal/products/list", request.getPage(), request.getSize()),
                        fetchDiscountListMono("/internal/discount-client/info")
                )
                )
                .flatMap(data -> redisService.saveData(cacheKey, data, DURATION_TTL).thenReturn(data));
    }

    // lấy danh sách product thuộc nhóm categorie từ redis, nếu không có trong redis sẽ call api để tổng hợp dữ liệu để đẩy lên redis
    public Mono<PaginationResponse<ProdWithDiscountResource>> getProductWithDiscountByCategorie(String categorieId, PaginationRequest request) {
        String cacheKey = String.format("prod:discount:cat:%s:%d:%d", categorieId, request.getPage(), request.getSize());
        final long DURATION_TTL = 3600;

        return redisService.getData(cacheKey, new ParameterizedTypeReference<PaginationResponse<ProdWithDiscountResource>>() {
        })
                .switchIfEmpty(fetchProductsWithDiscount(
                        fetchProductPageMonoByCategorie("/internal/products/categorie", categorieId, request.getPage(), request.getSize()),
                        fetchDiscountListMono("/internal/discount-client/info"))
                )
                .flatMap(data -> redisService.saveData(cacheKey, data, DURATION_TTL).thenReturn(data));
    }

    // lấy danh sách products có khuyến mãi từ redis, nếu không có trong redis sẽ call api để tổng hợp dữ liệu để đẩy lên redis
    public Mono<PaginationResponse<ProdWithDiscountResource>> getOnlyProductWithDiscount(PaginationRequest request) {
        String cacheKey = String.format("only:prod:discount:%d:%d", request.getPage(), request.getSize());
        final long DURATION_TTL = 3600;

        return redisService.getData(cacheKey, new ParameterizedTypeReference<PaginationResponse<ProdWithDiscountResource>>() {
        })
                .switchIfEmpty(fetchDiscountedProductsRecursive(request.getPage(), request.getSize(), new ArrayList<>()))
                .flatMap(data -> redisService.saveData(cacheKey, data, DURATION_TTL).thenReturn(data));
    }

    private Mono<PaginationResponse<ProdWithDiscountResource>> fetchDiscountedProductsRecursive(
            int page,
            int size,
            List<ProdWithDiscountResource> accumulatedProducts) {
        if (accumulatedProducts.size() >= size) {
            return Mono.just(new PaginationResponse<>(
                    accumulatedProducts.subList(0, size), page, size, 1, accumulatedProducts.size()
            ));
        }

        Mono<PaginationResponse<ProductClientResource>> productPageMono = fetchProductPageMono("/internal/products/list", page, size);
        Mono<List<DiscountClientResource>> discountListMono = fetchDiscountListMono("/internal/discount-client/info");

        return fetchProductandDiscount(productPageMono, discountListMono)
                .flatMap(paginationResponse -> {
                    List<ProdWithDiscountResource> filteredProducts = paginationResponse.getContent();
                    accumulatedProducts.addAll(filteredProducts);

                    if (paginationResponse.getContent().isEmpty()) {
                        // Không còn sản phẩm nào, trả về danh sách đã có
                        return Mono.just(new PaginationResponse<>(
                                accumulatedProducts, page, size, 1, accumulatedProducts.size()
                        ));
                    }

                    return fetchDiscountedProductsRecursive(page + 1, size, accumulatedProducts);
                });
    }

}
