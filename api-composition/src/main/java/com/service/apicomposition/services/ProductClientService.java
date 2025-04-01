package com.service.apicomposition.services;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

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
import com.service.apicomposition.resources.ProdAllInfoResource;
import com.service.apicomposition.resources.ProdWithDiscountAllInfoResource;
import com.service.apicomposition.resources.ProductClientResource;
import com.service.apicomposition.resources.ProdWithDiscountResource;
import com.service.apicomposition.responses.PaginationResponse;

import jakarta.ws.rs.core.UriBuilder;
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

    private Mono<PaginationResponse<ProductClientResource>> fetchProductByApparelTypePageMono(String path, int apparelType, int page, int size) {
        return productClient.get()
                .uri(uriBuilder -> uriBuilder
                .path(path)
                .queryParam("apparelType", apparelType)
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

    // lấy danh sách tất cả sản phẩm từ product-service
    private Mono<List<ProductClientResource>> fetchProductListMono(String path) {
        return productClient.get()
                .uri(path)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<ProductClientResource>>() {
                });
    }

    // lấy ra sản phẩm từ product-service từ id
    private Mono<ProdAllInfoResource> fectProductAllInfoMono(UUID id, String path) {
        return productClient.get()
                .uri(uriBuilder -> uriBuilder
                .path(path)
                .queryParam("id", id)
                .build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<ProdAllInfoResource>() {
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
    private Mono<List<ProdWithDiscountResource>> fetchProductandDiscount(
            Mono<List<ProductClientResource>> productListMono,
            Mono<List<DiscountClientResource>> discountListMono) {

        return Mono.zip(productListMono, discountListMono)
                .map(tuple -> {
                    List<ProductClientResource> products = tuple.getT1();
                    List<DiscountClientResource> discounts = tuple.getT2();

                    return products.stream()
                            .map(product -> {
                                DiscountClientResource discount = discounts.stream()
                                        .filter(d
                                                -> // Áp dụng cho tất cả sản phẩm (targetId null)
                                                (d.getTargetId() == null && TargetTypeEnum.PRODUCT.toString().equals(d.getTargetType()))
                                        // Hoặc áp dụng cho product cụ thể
                                        || (TargetTypeEnum.PRODUCT.toString().equals(d.getTargetType())
                                        && Objects.equals(d.getTargetId(), product.getId()))
                                        // Hoặc áp dụng theo category
                                        || (TargetTypeEnum.CATEGORIE.toString().equals(d.getTargetType())
                                        && Objects.equals(d.getTargetId(), product.getCategorieId()))
                                        )
                                        .findFirst()
                                        .orElse(null);

                                return discount != null
                                        ? productDiscountMapper.toProductWithDiscountResource(product, discount, componentMapper)
                                        : null;
                            })
                            .filter(Objects::nonNull)
                            .toList();
                })
                .doOnSuccess(result -> logger.info("Result: {}", result))
                .doOnError(error -> logger.error("Error: ", error));
    }

    /**
     * Fetch thông tin chi tiết của một sản phẩm kèm theo thông tin giảm giá phù
     * hợp.
     *
     * @param prodAllInfoMono Mono chứa thông tin đầy đủ của sản phẩm.
     * @param discountListMono Mono chứa danh sách tất cả các discount đang hoạt
     * động.
     * @return Mono chứa thông tin sản phẩm kèm discount (nếu có).
     */
    private Mono<ProdWithDiscountAllInfoResource> fetchProdWithDiscountAllInfoById(
            Mono<ProdAllInfoResource> prodAllInfoMono,
            Mono<List<DiscountClientResource>> discountListMono
    ) {
        return Mono.zip(prodAllInfoMono, discountListMono)
                .map(tuple -> {
                    ProdAllInfoResource product = tuple.getT1();
                    List<DiscountClientResource> discountList = tuple.getT2();

                    // Lọc ra discount phù hợp nhất cho sản phẩm này
                    DiscountClientResource discount = discountList.stream()
                            .filter(d
                                    -> // Discount áp dụng cho tất cả sản phẩm (targetId null)
                                    (d.getTargetId() == null && TargetTypeEnum.PRODUCT.toString().equals(d.getTargetType()))
                            // Hoặc áp dụng cho sản phẩm cụ thể
                            || (TargetTypeEnum.PRODUCT.toString().equals(d.getTargetType())
                            && Objects.equals(d.getTargetId(), product.getId()))
                            // Hoặc áp dụng theo category
                            || (TargetTypeEnum.CATEGORIE.toString().equals(d.getTargetType())
                            && Objects.equals(d.getTargetId(), product.getCategorieId()))
                            )
                            .findFirst()
                            .orElse(null); // Nếu không có discount phù hợp, trả về null

                    // Map dữ liệu sang ProdWithDiscountAllInfoResource để trả về
                    return productDiscountMapper.toProdWithDiscountAllInfoResource(product, discount, componentMapper);
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
        String cacheKey = "only:prod:discount:list";
        int start = request.getPage() * request.getSize();
        int end = start + request.getSize() - 1;

        return redisService.getListRange(cacheKey, start, end, ProdWithDiscountResource.class)
                .flatMap(result -> redisService.getListSize(cacheKey)
                .map(total -> {
                    int totalPages = (int) Math.ceil((double) total / request.getSize());
                    return PaginationResponse.<ProdWithDiscountResource>builder()
                            .content(result)
                            .pageNumber(request.getPage())
                            .pageSize(request.getSize())
                            .totalPages(totalPages)
                            .totalElements(total.intValue())
                            .build();
                }))
                .switchIfEmpty(syncDiscountedProductsToRedis()
                        .then(redisService.getListRange(cacheKey, start, end, ProdWithDiscountResource.class)
                                .flatMap(result -> redisService.getListSize(cacheKey)
                                .map(total -> {
                                    int totalPages = (int) Math.ceil((double) total / request.getSize());
                                    return PaginationResponse.<ProdWithDiscountResource>builder()
                                            .content(result)
                                            .pageNumber(request.getPage())
                                            .pageSize(request.getSize())
                                            .totalPages(totalPages)
                                            .totalElements(total.intValue())
                                            .build();
                                })))
                        .doOnError(e -> logger.error("Error fetching discounted products: {}", e.getMessage())));
    }

    private Mono<Void> syncDiscountedProductsToRedis() {
        String cacheKey = "only:prod:discount:list";
        long DURATION_TTL = 3600L;

        Mono<List<ProductClientResource>> productListMono = fetchProductListMono("/internal/products/all");
        Mono<List<DiscountClientResource>> discountListMono = fetchDiscountListMono("/internal/discount-client/info");

        return fetchProductandDiscount(productListMono, discountListMono)
                .flatMap(discountedProducts -> {
                    if (discountedProducts.isEmpty()) {
                        logger.warn("No discounted products found to sync");
                        return Mono.empty();
                    }
                    return redisService.saveList(cacheKey, discountedProducts, DURATION_TTL);
                })
                .then() // Trả về Mono<Void>
                .doOnSuccess(v -> logger.info("Synced discounted products to Redis"))
                .doOnError(e -> logger.error("Error syncing discounted products: {}", e.getMessage()));
    }

    // lấy danh sách product thuộc nhóm categorie theo apparelType từ redis, nếu không có trong redis sẽ call api để tổng hợp dữ liệu để đẩy lên redis
    public Mono<PaginationResponse<ProdWithDiscountResource>> getProdWithDiscountWithByCateApparelType(Integer apparelType, PaginationRequest request) {
        String cacheKey = String.format("prod:discount:apparel:%d:%d:%d", apparelType, request.getPage(), request.getSize());
        final long DURATION_TTL = 3600;

        return redisService.getData(cacheKey, new ParameterizedTypeReference<PaginationResponse<ProdWithDiscountResource>>() {
        })
                .switchIfEmpty(fetchProductsWithDiscount(
                        fetchProductByApparelTypePageMono("/internal/products/apparel-type", apparelType, request.getPage(), request.getSize()),
                        fetchDiscountListMono("/internal/discount-client/info")
                )
                )
                .flatMap(data -> redisService.saveData(cacheKey, data, DURATION_TTL).thenReturn(data));
    }

    /**
     * Lấy thông tin chi tiết của một sản phẩm kèm theo thông tin giảm giá áp
     * dụng (nếu có).
     *
     * @param productId UUID của sản phẩm cần lấy thông tin.
     * @return Mono<ProdWithDiscountAllInfoResource> chứa thông tin sản phẩm kèm
     * discount (nếu có).
     */
    public Mono<ProdWithDiscountAllInfoResource> getProdWithDiscountAllInfoById(UUID productId) {
        return fetchProdWithDiscountAllInfoById(
                // Fetch thông tin chi tiết của sản phẩm từ API nội bộ
                fectProductAllInfoMono(productId, "/internal/products/detail-info"),
                // Fetch danh sách các discount đang hoạt động từ API nội bộ
                fetchDiscountListMono("/internal/discount-client/info")
        );
    }

}
