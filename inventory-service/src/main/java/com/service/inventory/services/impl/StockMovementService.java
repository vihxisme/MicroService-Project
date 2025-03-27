package com.service.inventory.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    private Logger logger = LoggerFactory.getLogger(StockMovementService.class);

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "create-stockMovement", durable = "true"),
            exchange = @Exchange(name = "cache-create-stockMvm-exchange", type = "direct"),
            key = "create-stockMovement"
    ))
    private Boolean createStockMvmListener(List<StockMovementRequest> requests) {
        List<StockMovement> isList = createStockMovements(requests);

        return isList != null && !isList.isEmpty();
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
        logger.info("status: ");
        StockMovement existStockMovement = stockMovementRepository.findById(request.getId()).orElseThrow(()
                -> new EntityNotFoundException("StockeMovement not found"));

        logger.info("status: " + existStockMovement.getMovementStatus());

        stockMovementMapper.updateStockMovementFromRequest(request, existStockMovement);

        logger.info("status: " + existStockMovement.getMovementStatus());

        return stockMovementRepository.save(existStockMovement);
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

}
