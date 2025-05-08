package com.service.inventory.services.impl;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.service.inventory.entities.Inventory;
import com.service.events.dto.InventoryEvent;
import com.service.inventory.mappers.InventoryMapper;
import com.service.inventory.repositories.ApiClient;
import com.service.inventory.repositories.InventoryRepository;
import com.service.inventory.requests.InventoryItemRequest;
import com.service.inventory.requests.InventoryRequest;
import com.service.inventory.requests.PaginationRequest;
import com.service.inventory.resources.InventoryProductResource;
import com.service.inventory.resources.InventoryResource;
import com.service.inventory.responses.PaginationResponse;
import com.service.inventory.services.interfaces.InventoryInterface;
import com.service.inventory.wrappers.InventoryItemWrapper;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class InventoryService implements InventoryInterface {

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private InventoryMapper inventoryMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ApiClient apiClient;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RabbitService rabbitService;

    @Value("${rabbitmq.exchange.inventory}")
    private String exchange;

    private Logger logger = LoggerFactory.getLogger(InventoryService.class);

    @RabbitListener(queues = "create-inventory:queue")
    public void createInventoryListener(InventoryEvent event) {

        InventoryRequest request = inventoryMapper.toInventoryRequest(event.getInventoryDTO());

        Inventory inventory = createInventory(request);

        List<InventoryItemRequest> itemRequests = (List<InventoryItemRequest>) event.getProdVariantId().stream()
                .map(prodVariantId -> InventoryItemRequest.builder()
                .inventoryId(inventory.getId())
                .prodVariantId(prodVariantId)
                .build())
                .collect(Collectors.toList());
        rabbitTemplate.convertAndSend(exchange, "create-inventory-item", new InventoryItemWrapper(itemRequests));
    }

    @Override
    @Transactional
    public Inventory createInventory(InventoryRequest request) {
        Inventory inventory = inventoryMapper.toInventory(request);

        inventoryRepository.save(inventory);

        eventPublisher.publishEvent(inventory);

        return inventory;
    }

    @Override
    @Transactional
    public Inventory updateInventory(InventoryRequest request) {
        Inventory existInventory = inventoryRepository.findById(request.getId()).orElseThrow(()
                -> new EntityNotFoundException("Inventory not found"));

        inventoryMapper.updateInventoryFromRequest(request, existInventory);

        inventoryRepository.save(existInventory);

        eventPublisher.publishEvent(existInventory);

        return existInventory;
    }

    @Override
    @Transactional
    public Boolean deleteInventory(UUID inventoryId) {
        Inventory existInventory = inventoryRepository.findById(inventoryId).orElseThrow(()
                -> new EntityNotFoundException("Inventory not found"));

        inventoryRepository.delete(existInventory);

        eventPublisher.publishEvent(existInventory);

        return true;
    }

    @Override
    public PaginationResponse<InventoryResource> getPaginationInventory(PaginationRequest request) {
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());

        Page<Inventory> inventories = inventoryRepository.findAll(pageable);

        Page<InventoryResource> inventoryResources = inventories.map(inventoryMapper::toInventoryResource);

        return PaginationResponse.<InventoryResource>builder()
                .content(inventoryResources.getContent())
                .pageNumber(inventoryResources.getNumber())
                .pageSize(inventoryResources.getSize())
                .totalPages(inventoryResources.getTotalPages())
                .totalElements(inventoryResources.getTotalElements())
                .build();
    }

    @Override
    public PaginationResponse<InventoryProductResource> getInventoryProduct(PaginationRequest request) {
        ResponseEntity<?> response = apiClient.getInventoryProduct(request.getPage(), request.getSize());

        PaginationResponse<InventoryProductResource> inventoryProductResource = objectMapper.convertValue(response.getBody(),
                new TypeReference<PaginationResponse<InventoryProductResource>>() {
        });

        return inventoryProductResource;
    }

}
