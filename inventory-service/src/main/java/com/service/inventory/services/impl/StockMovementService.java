package com.service.inventory.services.impl;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.service.inventory.entities.StockMovement;
import com.service.inventory.enums.MovementEnum;
import com.service.inventory.mappers.StockMovementMapper;
import com.service.inventory.repositories.ApiClient;
import com.service.inventory.repositories.StockMovementRepository;
import com.service.inventory.requests.PaginationRequest;
import com.service.inventory.requests.StockMovementRequest;
import com.service.inventory.resources.StockMvmInProdResource;
import com.service.inventory.resources.StockMvmOutProdResource;
import com.service.inventory.resources.StockMovementResource;
import com.service.inventory.responses.PaginationResponse;
import com.service.inventory.services.interfaces.StockMovementInterface;
import com.service.inventory.wrappers.StockMvmWapper;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class StockMovementService implements StockMovementInterface {

    @Autowired
    private StockMovementRepository stockMovementRepository;

    @Autowired
    private StockMovementMapper stockMovementMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ApiClient apiClient;

    @Autowired
    private RabbitService rabbitService;

    private Logger logger = LoggerFactory.getLogger(StockMovementService.class);

    @RabbitListener(queues = "create-stock-mvm:queue")
    public void createStockMvmListener(Message message) {

        Optional<StockMvmWapper> requests = rabbitService.deserializeToObject(message.getBody(), StockMvmWapper.class);

        if (requests.isPresent()) {
            List<StockMovementRequest> stockMovementRequests = requests.get().getStockMvmRequests();
            createStockMovements(stockMovementRequests);
        }
    }

    @Override
    @Transactional
    public List<StockMovement> createStockMovements(List<StockMovementRequest> requests) {
        if (requests == null || requests.isEmpty()) {
            throw new IllegalArgumentException("List of requests cannot be null or empty");
        }

        List<StockMovement> stockMovements = new ArrayList<>();

        for (StockMovementRequest request : requests) {
            StockMovement stockMovement = stockMovementMapper.toStockMovement(request);
            if (stockMovement.getStockMovementCode() == null) {
                stockMovement.setStockMovementCode(generateStockMvmCode());
            }

            if (stockMovement.getOrderId() == null || stockMovement.getOrdersCode() == null) {
                stockMovement.setOrderId(null);
                stockMovement.setOrdersCode(null);
                stockMovement.setMovementType(MovementEnum.IN);
            } else {
                stockMovement.setMovementType(MovementEnum.OUT);
            }
            stockMovements.add(stockMovement);
        }

        return stockMovementRepository.saveAll(stockMovements);
    }

    @Override
    @Transactional
    public StockMovement updateStockMovement(StockMovementRequest request) {
        StockMovement existStockMovement = stockMovementRepository.findById(request.getId()).orElseThrow(()
                -> new EntityNotFoundException("StockeMovement not found"));

        stockMovementMapper.updateStockMovementFromRequest(request, existStockMovement);

        return stockMovementRepository.save(existStockMovement);
    }

    private String generateStockMvmCode() {
        Boolean isUnique;
        Random random = new Random();
        String code;
        do {
            int randomNumberStart = random.nextInt(100, 999);
            int randomNumberMiddle = random.nextInt(1000, 9999);
            int randomNumberEnd = random.nextInt(1000, 9999);
            code = String.format("%03d-%04d-%04d", randomNumberStart, randomNumberMiddle, randomNumberEnd);
            isUnique = !stockMovementRepository.existsByStockMovementCode(code);
        } while (!isUnique);

        return code;
    }

    @Override
    public PaginationResponse<StockMovementResource> getPaginationMovementTypeIN(PaginationRequest request) {
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());

        Page<StockMovementResource> stockMovements = stockMovementRepository.findAllTypeIN(pageable);

        return PaginationResponse.<StockMovementResource>builder()
                .content(stockMovements.getContent())
                .pageNumber(stockMovements.getNumber())
                .pageSize(stockMovements.getSize())
                .totalPages(stockMovements.getTotalPages())
                .totalElements(stockMovements.getTotalElements())
                .build();
    }

    @Override
    public PaginationResponse<StockMovementResource> getPaginationMovementTypeOUT(PaginationRequest request) {
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());

        Page<StockMovementResource> stockMovements = stockMovementRepository.findAllTypeOUT(pageable);

        return PaginationResponse.<StockMovementResource>builder()
                .content(stockMovements.getContent())
                .pageNumber(stockMovements.getNumber())
                .pageSize(stockMovements.getSize())
                .totalPages(stockMovements.getTotalPages())
                .totalElements(stockMovements.getTotalElements())
                .build();
    }

    @Override
    public PaginationResponse<StockMvmInProdResource> getStockMovementTypeIN_Product(PaginationRequest request) {
        ResponseEntity<?> response = apiClient.getStockMovementTypeIN_Product(request.getPage(), request.getSize());

        PaginationResponse<StockMvmInProdResource> stockMovementInProductResource = objectMapper.convertValue(response.getBody(),
                new TypeReference<PaginationResponse<StockMvmInProdResource>>() {
        });

        return stockMovementInProductResource;
    }

    @Override
    public PaginationResponse<StockMvmOutProdResource> getStockMovementTypeOUT_Product(PaginationRequest request) {
        ResponseEntity<?> response = apiClient.getStockMovementTypeOUT_Product(request.getPage(), request.getSize());

        PaginationResponse<StockMvmOutProdResource> stockMovementOutProductResource = objectMapper.convertValue(response.getBody(),
                new TypeReference<PaginationResponse<StockMvmOutProdResource>>() {
        });

        return stockMovementOutProductResource;
    }

    @Override
    public PaginationResponse<StockMovementResource> getPaginationMovementTypeIN(PaginationRequest request,
            UUID inventoryId) {
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), Sort.by("createdAt").descending());

        Page<StockMovementResource> stockMovements = stockMovementRepository.findTypeINByInventoryID(pageable, inventoryId);

        return PaginationResponse.<StockMovementResource>builder()
                .content(stockMovements.getContent())
                .pageNumber(stockMovements.getNumber())
                .pageSize(stockMovements.getSize())
                .totalPages(stockMovements.getTotalPages())
                .totalElements(stockMovements.getTotalElements())
                .build();
    }

    @Override
    public PaginationResponse<StockMovementResource> getPaginationMovementTypeOUT(PaginationRequest request,
            UUID inventoryId) {
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), Sort.by("createdAt").descending());

        Page<StockMovementResource> stockMovements = stockMovementRepository.findTypeOUTByInventoryId(pageable, inventoryId);

        return PaginationResponse.<StockMovementResource>builder()
                .content(stockMovements.getContent())
                .pageNumber(stockMovements.getNumber())
                .pageSize(stockMovements.getSize())
                .totalPages(stockMovements.getTotalPages())
                .totalElements(stockMovements.getTotalElements())
                .build();
    }

    @Override
    public PaginationResponse<StockMvmInProdResource> getStockMovementTypeIN_Product(PaginationRequest request,
            UUID inventoryId) {
        ResponseEntity<?> response = apiClient.getStockMovementTypeINbyInventoryId_Product(inventoryId, request.getPage(), request.getSize());

        PaginationResponse<StockMvmInProdResource> stockMovementInProductResource = objectMapper.convertValue(response.getBody(),
                new TypeReference<PaginationResponse<StockMvmInProdResource>>() {
        });

        return stockMovementInProductResource;
    }

    @Override
    public PaginationResponse<StockMvmOutProdResource> getStockMovementTypeOUT_Product(
            PaginationRequest request, UUID inventoryId) {
        ResponseEntity<?> response = apiClient.getStockMovementTypeOUTbyInventoryId_Product(inventoryId, request.getPage(), request.getSize());

        PaginationResponse<StockMvmOutProdResource> stockMovementOutProductResource = objectMapper.convertValue(response.getBody(),
                new TypeReference<PaginationResponse<StockMvmOutProdResource>>() {
        });

        return stockMovementOutProductResource;
    }

    @Override
    public PaginationResponse<StockMovementResource> getPaginationMvmType(PaginationRequest request, UUID inventoryId) {
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), Sort.by("createdAt").descending());

        Page<StockMovementResource> stockMovements = stockMovementRepository.findByInventoryID(pageable, inventoryId);

        return PaginationResponse.<StockMovementResource>builder()
                .content(stockMovements.getContent())
                .pageNumber(stockMovements.getNumber())
                .pageSize(stockMovements.getSize())
                .totalPages(stockMovements.getTotalPages())
                .totalElements(stockMovements.getTotalElements())
                .build();
    }

    @Override
    public PaginationResponse<StockMovementResource> getPaginationMvmType(PaginationRequest request, UUID inventoryId,
            String type) {

        MovementEnum movementType = MovementEnum.valueOf(type);

        Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), Sort.by("createdAt").descending());

        Page<StockMovementResource> stockMovements = stockMovementRepository.findTypeByInventoryID(pageable, inventoryId, movementType);

        return PaginationResponse.<StockMovementResource>builder()
                .content(stockMovements.getContent())
                .pageNumber(stockMovements.getNumber())
                .pageSize(stockMovements.getSize())
                .totalPages(stockMovements.getTotalPages())
                .totalElements(stockMovements.getTotalElements())
                .build();
    }

    @Override
    public PaginationResponse<StockMvmInProdResource> getStockMvmType_Product(PaginationRequest request,
            UUID inventoryId) {
        ResponseEntity<?> response = apiClient.getStockMovementTypebyInventoryId_Product(inventoryId, request.getPage(), request.getSize());

        PaginationResponse<StockMvmInProdResource> stockMovementInProductResource = objectMapper.convertValue(response.getBody(),
                new TypeReference<PaginationResponse<StockMvmInProdResource>>() {
        });

        return stockMovementInProductResource;
    }

    @Override
    public PaginationResponse<StockMvmInProdResource> getStockMvmType_Product(PaginationRequest request,
            UUID inventoryId, String type) {
        ResponseEntity<?> response = apiClient.getStockMovementTypebyInventoryId_Product(inventoryId, request.getPage(), request.getSize(), type);

        PaginationResponse<StockMvmInProdResource> stockMovementInProductResource = objectMapper.convertValue(response.getBody(),
                new TypeReference<PaginationResponse<StockMvmInProdResource>>() {
        });

        return stockMovementInProductResource;
    }

}
