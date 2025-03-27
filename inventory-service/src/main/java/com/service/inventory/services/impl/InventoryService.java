package com.service.inventory.services.impl;

import java.util.UUID;

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
import com.service.inventory.entities.Inventory;
import com.service.inventory.mappers.InventoryMapper;
import com.service.inventory.repositories.ApiClient;
import com.service.inventory.repositories.InventoryItemRepository;
import com.service.inventory.repositories.InventoryRepository;
import com.service.inventory.requests.InventoryRequest;
import com.service.inventory.requests.PaginationRequest;
import com.service.inventory.resources.InventoryProductResource;
import com.service.inventory.resources.InventoryResource;
import com.service.inventory.responses.PaginationResponse;
import com.service.inventory.services.interfaces.InventoryInterface;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class InventoryService implements InventoryInterface {

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private InventoryItemRepository inventoryItemRepository;

    @Autowired
    private InventoryMapper inventoryMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ApiClient apiClient;

    private Logger logger = LoggerFactory.getLogger(InventoryService.class);

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "create-inventory", durable = "true"),
            exchange = @Exchange(name = "cache-create-inventory-exchange", type = "direct"),
            key = "create-inventory"
    ))
    private Boolean createInventoryListener(InventoryRequest request) {
        Inventory isInventory = createInventory(request);

        return isInventory != null;
    }

    @Override
    @Transactional
    public Inventory createInventory(InventoryRequest request) {
        Inventory inventory = inventoryMapper.toInventory(request);

        return inventoryRepository.save(inventory);
    }

    @Override
    @Transactional
    public Inventory updateInventory(InventoryRequest request) {
        Inventory existInventory = inventoryRepository.findById(request.getId()).orElseThrow(()
                -> new EntityNotFoundException("Inventory not found"));

        inventoryMapper.updateInventoryFromRequest(request, existInventory);

        return inventoryRepository.save(existInventory);
    }

    @Override
    @Transactional
    public Boolean deleteInventory(UUID inventoryId) {
        Inventory existInventory = inventoryRepository.findById(inventoryId).orElseThrow(()
                -> new EntityNotFoundException("Inventory not found"));

        inventoryRepository.delete(existInventory);

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
