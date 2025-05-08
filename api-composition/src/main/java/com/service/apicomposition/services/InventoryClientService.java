package com.service.apicomposition.services;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.service.apicomposition.commons.HasProdVariant;
import com.service.apicomposition.commons.HasProduct;
import com.service.apicomposition.dtos.InvenItemCheckStock;
import com.service.apicomposition.dtos.ProductInvenDTO;
import com.service.apicomposition.mappers.InventoryClientMapper;
import com.service.apicomposition.requests.PaginationRequest;
import com.service.apicomposition.resources.InventoryClientResource;
import com.service.apicomposition.resources.InventoryProdResource;
import com.service.apicomposition.resources.ItemClientResource;
import com.service.apicomposition.resources.ItemProdVariantResource;
import com.service.apicomposition.resources.ProdVariantResource;
import com.service.apicomposition.resources.StockMvmClientResource;
import com.service.apicomposition.resources.StockMvmInProdResource;
import com.service.apicomposition.resources.StockMvmOutProdResource;
import com.service.apicomposition.responses.PaginationResponse;

import reactor.core.publisher.Mono;

@Service
public class InventoryClientService {

    @Autowired
    @Qualifier("productWebClient")
    private WebClient productClient;

    @Autowired
    @Qualifier("inventoryWebClient")
    private WebClient inventoryClient;

    @Autowired
    private InventoryClientMapper inventoryClientMapper;

    @Autowired
    private RedisService redisService;

    private final long DURATION_TTL = 3600;

    private Logger logger = LoggerFactory.getLogger(InventoryClientService.class);

    // lấy danh sách InventoryClientResource
    private Mono<PaginationResponse<InventoryClientResource>> fetchInventoryPageMono(String path, int page, int size) {
        return inventoryClient.get()
                .uri(uriBuilder -> uriBuilder
                .path(path)
                .queryParam("page", page)
                .queryParam("size", size)
                .build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<PaginationResponse<InventoryClientResource>>() {
                });
    }

    // lấy tên sản phẩm từ T.productID trong InventoryClientResource
    private <T extends HasProduct> Mono<Map<UUID, String>> productNamesMono(List<T> inventoryList, String path) {
        return inventoryList.isEmpty() ? Mono.just(Collections.emptyMap())
                : productClient.get()
                        .uri(uriBuilder -> uriBuilder.path(path)
                        .queryParam("productIds", inventoryList.stream()
                                .map(T::getProductId)
                                .collect(Collectors.toList()))
                        .build())
                        .retrieve()
                        .bodyToMono(new ParameterizedTypeReference<Map<UUID, String>>() {
                        });
    }

    private Mono<Map<UUID, ProductInvenDTO>> productInven(List<InventoryClientResource> inventoryList, String path) {
        return inventoryList.isEmpty() ? Mono.just(Collections.emptyMap())
                : productClient.get()
                        .uri(uriBuilder -> uriBuilder.path(path)
                        .queryParam("productIds", inventoryList.stream()
                                .map(InventoryClientResource::getProductId)
                                .collect(Collectors.toList()))
                        .build())
                        .retrieve()
                        .bodyToMono(new ParameterizedTypeReference<Map<UUID, ProductInvenDTO>>() {
                        });
    }

    // lấy danh sách StockMvmClientResource
    private Mono<PaginationResponse<StockMvmClientResource>> fetchStockMvmPageMono(String path, int page, int size) {
        return inventoryClient.get()
                .uri(uriBuilder -> uriBuilder
                .path(path)
                .queryParam("page", page)
                .queryParam("size", size)
                .build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<PaginationResponse<StockMvmClientResource>>() {
                });
    }

    // lấy danh sách StockMvmClientResource theo inventoryId
    private Mono<PaginationResponse<StockMvmClientResource>> fetchStockMvmPageMono(String path, UUID inventoryId, int page, int size) {
        return inventoryClient.get()
                .uri(uriBuilder -> uriBuilder
                .path(path)
                .queryParam("inventoryId", inventoryId)
                .queryParam("page", page)
                .queryParam("size", size)
                .build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<PaginationResponse<StockMvmClientResource>>() {
                });
    }

    // lấy danh sách StockMvmClientResource theo inventoryId
    private Mono<PaginationResponse<StockMvmClientResource>> fetchStockMvmPageMono(String path, UUID inventoryId, String type, int page, int size) {
        return inventoryClient.get()
                .uri(uriBuilder -> uriBuilder
                .path(path)
                .queryParam("inventoryId", inventoryId)
                .queryParam("type", type)
                .queryParam("page", page)
                .queryParam("size", size)
                .build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<PaginationResponse<StockMvmClientResource>>() {
                });
    }

    // lấy danh sách ProdVariantResource từ T.getProdVariantId
    private <T extends HasProdVariant> Mono<List<ProdVariantResource>> fecthProdVariantMono(List<T> stockMvmList, String path) {
        return stockMvmList.isEmpty() ? Mono.just(Collections.emptyList())
                : productClient.get()
                        .uri(uriBuilder -> uriBuilder.path(path)
                        .queryParam("variantIds", stockMvmList.stream()
                                .map(T::getProdVariantId)
                                .collect(Collectors.toList()))
                        .build())
                        .retrieve()
                        .bodyToMono(new ParameterizedTypeReference<List<ProdVariantResource>>() {
                        });
    }

    // lấy danh sách ItemClientResource
    private Mono<PaginationResponse<ItemClientResource>> fetchItemPagesMono(String path, String inventoryId, int page, int size) {
        return inventoryClient.get()
                .uri(uriBuilder -> uriBuilder
                .path(path)
                .queryParam("inventoryId", inventoryId)
                .queryParam("page", page)
                .queryParam("size", size)
                .build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<PaginationResponse<ItemClientResource>>() {
                });
    }

    // tổng hợp dữ liệu từ productNamesMono và fetchInventoryPageMono
    private Mono<PaginationResponse<InventoryProdResource>> fetchInventoryWithProduct(
            Mono<PaginationResponse<InventoryClientResource>> inventoryListMono) {
        return inventoryListMono.flatMap(inventoryPage -> {
            List<InventoryClientResource> inventoryList = inventoryPage.getContent();

            // Tạo request lấy tên sản phẩm và danh mục
            //     Mono<Map<UUID, String>> productNamesMono = productNamesMono(inventoryList, "/internal/product-names");
            Mono<Map<UUID, ProductInvenDTO>> productInvenMono = productInven(inventoryList, "/internal/products/inven");

            return productInvenMono.map(product -> {
                List<InventoryProdResource> inventoryProductResource = inventoryList.stream()
                        .map(inventory -> {
                            ProductInvenDTO productInvenDto = product.getOrDefault(inventory.getProductId(), null);
                            return inventoryClientMapper.toInventoryProductResource(inventory, productInvenDto);
                        })
                        .collect(Collectors.toList());

                return PaginationResponse.<InventoryProdResource>builder()
                        .content(inventoryProductResource)
                        .pageNumber(inventoryPage.getPageNumber())
                        .pageSize(inventoryPage.getPageSize())
                        .totalPages(inventoryPage.getTotalPages())
                        .totalElements(inventoryPage.getTotalElements())
                        .build();
            })
                    .doOnSuccess(result -> logger.info("Result: {}", result))
                    .doOnError(error -> logger.error("Error: ", error));
        });
    }

    // tổng hợp dữ liệu từ StockMvmClientResource, ProdVariantResource và productNamesFromStockMono ==> StockMvmInProdResource
    private Mono<PaginationResponse<StockMvmInProdResource>> fetchStockMvmInProd(
            Mono<PaginationResponse<StockMvmClientResource>> stockMvmListMono
    ) {
        return stockMvmListMono.flatMap(stockMvmPages -> {

            List<StockMvmClientResource> stockMvmList = stockMvmPages.getContent();

            Mono<List<ProdVariantResource>> prodVariantListMono = fecthProdVariantMono(stockMvmList, "/internal/prod-variant");
            Mono<Map<UUID, String>> productNamesMono = productNamesMono(stockMvmList, "/internal/product-names");

            return Mono.zip(prodVariantListMono, productNamesMono)
                    .map(tuple -> {

                        List<ProdVariantResource> prodVariantList = tuple.getT1();
                        Map<UUID, String> productNames = tuple.getT2();

                        // Lấy productNamesMono (phải dùng flatMap vì đây là một Mono)
                        List<StockMvmInProdResource> stockMvmClientResources = stockMvmPages.getContent()
                                .stream()
                                .map(stock -> {
                                    // Lấy tên sản phẩm từ productNames
                                    String productName = productNames.getOrDefault(stock.getProductId(), "Unknown Product");

                                    // Tìm ProdVariantResource tương ứng
                                    ProdVariantResource prodVariant = prodVariantList.stream()
                                            .filter(p -> p.getId() == stock.getProdVariantId())
                                            .findFirst()
                                            .orElse(null);

                                    return inventoryClientMapper.toStockMvmInProdResource(stock, prodVariant, productName);
                                })
                                .filter(Objects::nonNull)
                                .toList();

                        return PaginationResponse.<StockMvmInProdResource>builder()
                                .content(stockMvmClientResources)
                                .pageNumber(stockMvmPages.getPageNumber())
                                .pageSize(stockMvmPages.getPageSize())
                                .totalPages(stockMvmPages.getTotalPages())
                                .totalElements(stockMvmPages.getTotalElements())
                                .build();
                    });
        });
    }

    // tổng hợp dữ liệu từ fetchItemPagesMono và fecthProdVariantMono
    private Mono<PaginationResponse<ItemProdVariantResource>> fetchItemProdVariant(
            Mono<PaginationResponse<ItemClientResource>> itemClientListMono) {
        return itemClientListMono.flatMap(itemClientPages -> {
            List<ItemClientResource> itemClientList = itemClientPages.getContent();
            Mono<List<ProdVariantResource>> prodVariantListMono = fecthProdVariantMono(itemClientList, "/internal/prod-variant");

            return prodVariantListMono.map(prodVariantList -> {
                List<ItemProdVariantResource> itemProdVariant = itemClientList.stream()
                        .map(item -> {
                            ProdVariantResource prodVariantResource = prodVariantList.stream()
                                    .filter(pv -> pv.getId() == item.getProdVariantId())
                                    .findFirst()
                                    .orElse(null);

                            return inventoryClientMapper.toItemProdVariantResource(item, prodVariantResource);
                        })
                        .collect(Collectors.toList());

                return PaginationResponse.<ItemProdVariantResource>builder()
                        .content(itemProdVariant)
                        .pageNumber(itemClientPages.getPageNumber())
                        .pageSize(itemClientPages.getPageSize())
                        .totalPages(itemClientPages.getTotalPages())
                        .totalElements(itemClientPages.getTotalElements())
                        .build();
            });
        });
    }

    // tổng hợp dữ liệu từ StockMvmClientResource, ProdVariantResource và productNamesFromStockMono ==> StockMvmOutProdResource
    private Mono<PaginationResponse<StockMvmOutProdResource>> fetchStockMvmOutProd(
            Mono<PaginationResponse<StockMvmClientResource>> stockMvmListMono
    ) {
        return stockMvmListMono.flatMap(stockMvmPages -> {

            List<StockMvmClientResource> stockMvmList = stockMvmPages.getContent();

            Mono<List<ProdVariantResource>> prodVariantListMono = fecthProdVariantMono(stockMvmList, "/internal/prod-variant");
            Mono<Map<UUID, String>> productNamesMono = productNamesMono(stockMvmList, "/internal/product-names");

            return Mono.zip(prodVariantListMono, productNamesMono)
                    .map(tuple -> {

                        List<ProdVariantResource> prodVariantList = tuple.getT1();
                        Map<UUID, String> productNames = tuple.getT2();

                        // Lấy productNamesMono (phải dùng flatMap vì đây là một Mono)
                        List<StockMvmOutProdResource> stockMvmClientResources = stockMvmPages.getContent()
                                .stream()
                                .map(stock -> {
                                    // Lấy tên sản phẩm từ productNames
                                    String productName = productNames.getOrDefault(stock.getProductId(), "Unknown Product");

                                    // Tìm ProdVariantResource tương ứng
                                    ProdVariantResource prodVariant = prodVariantList.stream()
                                            .filter(p -> p.getId() == stock.getProdVariantId())
                                            .findFirst()
                                            .orElse(null);

                                    return inventoryClientMapper.toStockMvmOutProdResource(stock, prodVariant, productName);
                                })
                                .filter(Objects::nonNull)
                                .toList();

                        return PaginationResponse.<StockMvmOutProdResource>builder()
                                .content(stockMvmClientResources)
                                .pageNumber(stockMvmPages.getPageNumber())
                                .pageSize(stockMvmPages.getPageSize())
                                .totalPages(stockMvmPages.getTotalPages())
                                .totalElements(stockMvmPages.getTotalElements())
                                .build();
                    })
                    .doOnSuccess(result -> logger.info("Result: {}", result))
                    .doOnError(error -> logger.error("Error: ", error));
        });
    }

    // lấy dữ liệu tổng hợp được từ fetchInventoryWithProduct trong redis, nếu không có trong redis thì lưu dữ liệu vào redis
    public Mono<PaginationResponse<InventoryProdResource>> getInventoryWithProduct(PaginationRequest request) {
        String cacheKey = String.format("inven:prod:%d:%d", request.getPage(), request.getSize());

        return redisService.getData(cacheKey, new ParameterizedTypeReference<PaginationResponse<InventoryProdResource>>() {
        })
                .switchIfEmpty(fetchInventoryWithProduct(fetchInventoryPageMono("/internal/inventory", request.getPage(), request.getSize())))
                .flatMap(data -> redisService.saveData(cacheKey, data, DURATION_TTL).thenReturn(data));
    }

    // lấy dữ liệu tổng hợp được từ fetchStockMvmInProd trong redis, nếu không có trong redis thì lưu dữ liệu vào redis
    public Mono<PaginationResponse<StockMvmInProdResource>> getStockMvmInProd(PaginationRequest request) {
        String cacheKey = String.format("inven:stock:in:prod:%d:%d", request.getPage(), request.getSize());

        return redisService.getData(cacheKey, new ParameterizedTypeReference<PaginationResponse<StockMvmInProdResource>>() {
        })
                .switchIfEmpty(fetchStockMvmInProd(
                        fetchStockMvmPageMono("/internal/stock-movement/type-in", request.getPage(), request.getSize())
                ))
                .flatMap(data -> redisService.saveData(cacheKey, data, DURATION_TTL).thenReturn(data));
    }

    // lấy dữ liệu tổng hợp được từ fetchStockMvmOutProd trong redis, nếu không có trong redis thì lưu dữ liệu vào redis
    public Mono<PaginationResponse<StockMvmOutProdResource>> getStockMvmOutProd(PaginationRequest request) {
        String cacheKey = String.format("inven:stock:out:prod:%d:%d", request.getPage(), request.getSize());

        return redisService.getData(cacheKey, new ParameterizedTypeReference<PaginationResponse<StockMvmOutProdResource>>() {
        })
                .switchIfEmpty(fetchStockMvmOutProd(
                        fetchStockMvmPageMono("/internal/stock-movement/type-out", request.getPage(), request.getSize())
                ))
                .flatMap(data -> redisService.saveData(cacheKey, data, DURATION_TTL).thenReturn(data))
                .doOnError(e -> logger.error("[getStockMvmOutProd] Error occurred: {}", e.getMessage(), e));
    }

    // lấy dữ liệu tổng được từ fetchItemProdVariant trong redis, nếu không có trong redis thì lưu dữ liệu vào redis
    public Mono<PaginationResponse<ItemProdVariantResource>> getItemProdVariant(String inventoryId, PaginationRequest request) {
        String cacheKey = String.format("inven:item:prod:variant:%s:%d:%d", inventoryId, request.getPage(), request.getSize());

        return redisService.getData(cacheKey, new ParameterizedTypeReference<PaginationResponse<ItemProdVariantResource>>() {
        })
                .switchIfEmpty(fetchItemProdVariant(
                        fetchItemPagesMono("/internal/inventory-items", inventoryId, request.getPage(), request.getSize())
                ))
                .flatMap(data -> redisService.saveData(cacheKey, data, DURATION_TTL).thenReturn(data));
    }

    public Mono<Map<Integer, Boolean>> checkInventoryItem(List<InvenItemCheckStock> inventoryItemIds) {
        return inventoryClient.post()
                .uri("/internal/inven-item/check-stock")
                .bodyValue(inventoryItemIds)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<Integer, Boolean>>() {
                });
    }

    // lấy dữ liệu tổng hợp được từ fetchStockMvmInProd trong redis, nếu không có trong redis thì lưu dữ liệu vào redis
    public Mono<PaginationResponse<StockMvmInProdResource>> getStockMvmInProd(UUID inventoryId, PaginationRequest request) {
        String cacheKey = String.format("inven:stock:in:%s:%d:%d", inventoryId, request.getPage(), request.getSize());

        return redisService.getData(cacheKey, new ParameterizedTypeReference<PaginationResponse<StockMvmInProdResource>>() {
        })
                .switchIfEmpty(fetchStockMvmInProd(
                        fetchStockMvmPageMono("/internal/stock-movement/type-in/by", inventoryId, request.getPage(), request.getSize())
                ))
                .flatMap(data -> redisService.saveData(cacheKey, data, DURATION_TTL).thenReturn(data));
    }

    // lấy dữ liệu tổng hợp được từ fetchStockMvmOutProd trong redis, nếu không có trong redis thì lưu dữ liệu vào redis
    public Mono<PaginationResponse<StockMvmOutProdResource>> getStockMvmOutProd(UUID inventoryId, PaginationRequest request) {
        String cacheKey = String.format("inven:stock:out:%s:%d:%d", inventoryId, request.getPage(), request.getSize());

        return redisService.getData(cacheKey, new ParameterizedTypeReference<PaginationResponse<StockMvmOutProdResource>>() {
        })
                .switchIfEmpty(fetchStockMvmOutProd(
                        fetchStockMvmPageMono("/internal/stock-movement/type-out/by", inventoryId, request.getPage(), request.getSize())
                ))
                .flatMap(data -> redisService.saveData(cacheKey, data, DURATION_TTL).thenReturn(data));
    }

    public Mono<PaginationResponse<StockMvmInProdResource>> getStockMvmTypeProd(UUID inventoryId, PaginationRequest request) {
        String cacheKey = String.format("inven:stock:%s:%d:%d", inventoryId, request.getPage(), request.getSize());

        return redisService.getData(cacheKey, new ParameterizedTypeReference<PaginationResponse<StockMvmInProdResource>>() {
        })
                .switchIfEmpty(fetchStockMvmInProd(
                        fetchStockMvmPageMono("/internal/stock-movement/type-all", inventoryId, request.getPage(), request.getSize())
                ))
                .flatMap(data -> redisService.saveData(cacheKey, data, DURATION_TTL).thenReturn(data));
    }

    public Mono<PaginationResponse<StockMvmInProdResource>> getStockMvmTypeProd(UUID inventoryId, PaginationRequest request, String type) {
        String cacheKey = String.format("inven:stock:%s:%s:%d:%d", inventoryId, type, request.getPage(), request.getSize());

        return redisService.getData(cacheKey, new ParameterizedTypeReference<PaginationResponse<StockMvmInProdResource>>() {
        })
                .switchIfEmpty(fetchStockMvmInProd(
                        fetchStockMvmPageMono("/internal/stock-movement/type", inventoryId, type, request.getPage(), request.getSize())
                ))
                .flatMap(data -> redisService.saveData(cacheKey, data, DURATION_TTL).thenReturn(data))
                .doOnError(e -> logger.error("[getStockMvmOutProd] Error occurred: {}", e.getMessage(), e));
    }

}
