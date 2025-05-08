package com.service.apicomposition.controllers;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.service.apicomposition.dtos.InvenItemCheckStock;
import com.service.apicomposition.requests.PaginationRequest;
import com.service.apicomposition.services.InventoryClientService;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/inventory-client")
public class API_InventoryClientController {

    @Autowired
    private InventoryClientService inventoryClientService;

    @Autowired
    private ObjectMapper objectMapper;

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

    @GetMapping("/stock-movement/type-in/by")
    public Mono<ResponseEntity<Object>> getStockMvmInProdByInventoryId(ServerWebExchange exchange) {
        // Lấy thông tin phân trang từ query parameters
        UUID inventoryId = UUID.fromString(exchange.getRequest().getQueryParams().getFirst("inventoryId"));
        int page = Integer.parseInt(exchange.getRequest().getQueryParams().getFirst("page"));
        int size = Integer.parseInt(exchange.getRequest().getQueryParams().getFirst("size"));
        PaginationRequest request = PaginationRequest.builder().page(page).size(size).build();

        return inventoryClientService.getStockMvmInProd(inventoryId, request)
                .map(inventoryProduct -> ResponseEntity.ok((Object) inventoryProduct))
                .onErrorResume(ex -> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Network Error")));
    }

    @GetMapping("/stock-movement/type-out/by")
    public Mono<ResponseEntity<Object>> getStockMvmOutProdByInventoryId(ServerWebExchange exchange) {
        // Lấy thông tin phân trang từ query parameters
        UUID inventoryId = UUID.fromString(exchange.getRequest().getQueryParams().getFirst("inventoryId"));
        int page = Integer.parseInt(exchange.getRequest().getQueryParams().getFirst("page"));
        int size = Integer.parseInt(exchange.getRequest().getQueryParams().getFirst("size"));
        PaginationRequest request = PaginationRequest.builder().page(page).size(size).build();

        return inventoryClientService.getStockMvmOutProd(inventoryId, request)
                .map(inventoryProduct -> ResponseEntity.ok((Object) inventoryProduct))
                .onErrorResume(ex -> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Network Error")));
    }

    @GetMapping("/stock-movement/type-all")
    public Mono<ResponseEntity<Object>> getStockMvmTypeProdByInventoryId(ServerWebExchange exchange) {
        // Lấy thông tin phân trang từ query parameters
        UUID inventoryId = UUID.fromString(exchange.getRequest().getQueryParams().getFirst("inventoryId"));
        int page = Integer.parseInt(exchange.getRequest().getQueryParams().getFirst("page"));
        int size = Integer.parseInt(exchange.getRequest().getQueryParams().getFirst("size"));
        PaginationRequest request = PaginationRequest.builder().page(page).size(size).build();

        return inventoryClientService.getStockMvmTypeProd(inventoryId, request)
                .map(inventoryProduct -> ResponseEntity.ok((Object) inventoryProduct))
                .onErrorResume(ex -> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Network Error")));
    }

    @GetMapping("/stock-movement/type")
    public Mono<ResponseEntity<Object>> getStockMvmTypeInputProdByInventoryId(ServerWebExchange exchange) {
        // Lấy thông tin phân trang từ query parameters
        UUID inventoryId = UUID.fromString(exchange.getRequest().getQueryParams().getFirst("inventoryId"));
        String type = exchange.getRequest().getQueryParams().getFirst("type");
        int page = Integer.parseInt(exchange.getRequest().getQueryParams().getFirst("page"));
        int size = Integer.parseInt(exchange.getRequest().getQueryParams().getFirst("size"));
        PaginationRequest request = PaginationRequest.builder().page(page).size(size).build();

        return inventoryClientService.getStockMvmTypeProd(inventoryId, request, type)
                .map(inventoryProduct -> ResponseEntity.ok((Object) inventoryProduct))
                .onErrorResume(ex -> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Network Error")));
    }

    @PostMapping("/inven-item/check-stock")
    public Mono<ResponseEntity<Object>> checkInventoryItem(ServerWebExchange exchange) {
        return exchange.getRequest().getBody()
                .map(dataBuffer -> {
                    byte[] bytes = new byte[dataBuffer.readableByteCount()];
                    dataBuffer.read(bytes);
                    DataBufferUtils.release(dataBuffer); // Giải phóng bộ nhớ
                    return new String(bytes, StandardCharsets.UTF_8);
                })
                .collectList() // Thu thập tất cả DataBuffers thành một danh sách
                .map(buffers -> String.join("", buffers)) // Ghép tất cả các chuỗi lại với nhau
                .flatMap(body -> {
                    try {
                        List<InvenItemCheckStock> checkStockList = objectMapper.readValue(body, new TypeReference<List<InvenItemCheckStock>>() {
                        });
                        return Mono.just(checkStockList);
                    } catch (IOException e) {
                        return Mono.error(e);
                    }
                })
                .flatMap(checkStockList -> inventoryClientService.checkInventoryItem(checkStockList)
                .map(resultMap -> ResponseEntity.ok((Object) resultMap))
                .onErrorResume(ex -> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Network Error")))
                );

    }

}
