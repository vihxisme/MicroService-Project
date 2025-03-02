package com.service.inventory.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
import com.service.inventory.entities.InventoryItem;
import com.service.inventory.entities.StockMovement;
import com.service.inventory.mappers.InventoryItemMapper;
import com.service.inventory.repositories.ApiClient;
import com.service.inventory.repositories.InventoryItemRepository;
import com.service.inventory.requests.InventoryItemRequest;
import com.service.inventory.requests.PaginationRequest;
import com.service.inventory.requests.StockMovementRequest;
import com.service.inventory.resources.ItemProdVariantResource;
import com.service.inventory.resources.ItemResource;
import com.service.inventory.responses.PaginationResponse;
import com.service.inventory.services.interfaces.InventoryItemInterface;

import jakarta.persistence.EntityNotFoundException;

@Service
public class InventoryItemService implements InventoryItemInterface {

    @Autowired
    private InventoryItemRepository inventoryItemRepository;

    @Autowired
    private InventoryItemMapper inventoryItemMapper;

    @Autowired
    private ApiClient apiClient;

    @Autowired
    private ObjectMapper objectMapper;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "create-stockMovement", durable = "true"),
            exchange = @Exchange(name = "cache-create-stockMvm-exchange", type = "direct"),
            key = "create-stockMovement"
    ))
    private Boolean createStockMvmListener(List<InventoryItemRequest> requests) {
        List<InventoryItem> isList = createInventoryItem(requests);

        return isList != null && !isList.isEmpty();
    }

    @Override
    public List<InventoryItem> createInventoryItem(List<InventoryItemRequest> requests) {

        if (requests == null || requests.isEmpty()) {
            throw new IllegalArgumentException("List of requests cannot be null or empty");
        }

        List<InventoryItem> inventoryItems = new ArrayList<>();

        for (InventoryItemRequest request : requests) {
            InventoryItem inventoryItem = inventoryItemMapper.toInInventoryItem(request);

            inventoryItems.add(inventoryItem);
        }

        return inventoryItemRepository.saveAll(inventoryItems);
    }

    @Override
    public InventoryItem updateInventoryItem(InventoryItemRequest request) {
        InventoryItem existInventoryItem = inventoryItemRepository.findById(request.getId()).orElseThrow(()
                -> new EntityNotFoundException("InventoryItem not found"));

        inventoryItemMapper.updateInventoryItemFromRequest(request, existInventoryItem);

        inventoryItemRepository.save(existInventoryItem);

        return existInventoryItem;
    }

    @Override
    public Boolean deleteInventoryItem(UUID id) {
        InventoryItem existInventoryItem = inventoryItemRepository.findById(id).orElseThrow(()
                -> new EntityNotFoundException("InventoryItem not found"));

        inventoryItemRepository.delete(existInventoryItem);

        return true;
    }

    @Override
    public InventoryItem inventoryIntake(UUID id, int quantity) {
        InventoryItem inventoryItem = inventoryItemRepository.findById(id).orElseThrow(()
                -> new EntityNotFoundException("InventoryItem not found"));

        int itemQuantity = inventoryItem.getItemQuantity() + quantity;

        inventoryItem.setItemQuantity(itemQuantity);

        inventoryItemRepository.save(inventoryItem);

        return inventoryItem;
    }

    @Override
    public PaginationResponse<ItemResource> getPaginationInventoryItem(UUID inventoryId, PaginationRequest request) {
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());

        Page<InventoryItem> inventoryItems = inventoryItemRepository.findAll(inventoryId, pageable);

        Page<ItemResource> itemResources = inventoryItems.map(inventoryItemMapper::toItemResource);

        return PaginationResponse.<ItemResource>builder()
                .content(itemResources.getContent())
                .pageNumber(itemResources.getNumber())
                .pageSize(itemResources.getSize())
                .totalPages(itemResources.getTotalPages())
                .totalElements(itemResources.getTotalElements())
                .build();
    }

    @Override
    public PaginationResponse<ItemProdVariantResource> getPaginationItemProdVariant(String inventoryId, PaginationRequest request) {
        ResponseEntity<?> response = apiClient.getItemProductVariant(inventoryId, request.getPage(), request.getSize());

        PaginationResponse<ItemProdVariantResource> itemProdVariantResource = objectMapper.convertValue(response.getBody(),
                new TypeReference<PaginationResponse<ItemProdVariantResource>>() {
        });

        return itemProdVariantResource;
    }

}
