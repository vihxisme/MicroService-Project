package com.service.inventory.services.impl;

import java.util.ArrayList;
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
import com.service.events.dto.InvenItemStockDTO;
import com.service.inventory.dtos.InvenItemCheckStock;
import com.service.inventory.entities.InventoryItem;
import com.service.inventory.enums.MovementEnum;
import com.service.inventory.mappers.InventoryItemMapper;
import com.service.inventory.repositories.ApiClient;
import com.service.inventory.repositories.InventoryItemRepository;
import com.service.inventory.repositories.InventoryRepository;
import com.service.inventory.requests.InventoryItemRequest;
import com.service.inventory.requests.PaginationRequest;
import com.service.inventory.requests.StockMovementRequest;
import com.service.inventory.resources.ItemProdVariantResource;
import com.service.inventory.resources.ItemResource;
import com.service.inventory.responses.PaginationResponse;
import com.service.inventory.services.interfaces.InventoryItemInterface;
import com.service.inventory.wrappers.InventoryItemWrapper;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class InventoryItemService implements InventoryItemInterface {

    @Autowired
    private InventoryItemRepository inventoryItemRepository;

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private InventoryItemMapper inventoryItemMapper;

    @Autowired
    private ApiClient apiClient;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Autowired
    private RabbitService rabbitService;

    @Value("${rabbitmq.exchange.inventory}")
    private String myExchange;

    private Logger logger = LoggerFactory.getLogger(InventoryItemService.class);

    @RabbitListener(queues = "create-inventory-item:queue")
    public void createInventoryItemListener(InventoryItemWrapper wrapper) {
        createInventoryItem(wrapper.getInventoryItemRequests());
    }

    @RabbitListener(queues = "update-inventory-item:queue")
    @Transactional
    public void updateStockForItemListener(Message message) {
        try {
            Optional<Map<Integer, Integer>> mapReceive = rabbitService.deserializeToMap(message.getBody(), new TypeReference<Map<Integer, Integer>>() {
            });

            if (mapReceive.isEmpty()) {
                logger.warn("Invalid message format received in update-inventory-item:queue");
                return;
            }

            Map<Integer, Integer> updateMap = mapReceive.get();
            List<Integer> prodVariantIds = new ArrayList<>(updateMap.keySet());

            List<InventoryItem> inventoryItems = inventoryItemRepository.findByProdVariantIds(prodVariantIds);

            if (inventoryItems.size() != prodVariantIds.size()) {
                List<Integer> foundIds = inventoryItems.stream()
                        .map(InventoryItem::getProdVariantId)
                        .toList();
                List<Integer> missingIds = prodVariantIds.stream()
                        .filter(id -> !foundIds.contains(id))
                        .toList();

                logger.warn("Inventory update skipped. Missing InventoryItems for IDs: {}", missingIds);
                return;
            }

            // Thực hiện trừ tồn kho
            for (InventoryItem inventoryItem : inventoryItems) {
                Integer subtractQty = updateMap.get(inventoryItem.getProdVariantId());
                if (subtractQty != null) {
                    int newQuantity = inventoryItem.getItemQuantity() - subtractQty;
                    inventoryItem.setItemQuantity(Math.max(newQuantity, 0)); // Không cho âm nếu cần
                }
            }

            inventoryItemRepository.saveAll(inventoryItems);
            eventPublisher.publishEvent(inventoryItems);

            logger.info("Updated inventory for product variant IDs: {}", prodVariantIds);

        } catch (Exception e) {
            logger.error("Error while updating inventory from message", e);
        }
    }

    @RabbitListener(queues = "inventory-item-stock:queue")
    public void inventoryItemListener(Message message) {
        // Optional<List<InvenItemStockDTO>> responseMessage
        //         = rabbitService.deserializeToObject(message.getBody(), new TypeReference<List<InvenItemStockDTO>>() {
        //         });

        // if (responseMessage.isPresent()) {
        //     List<InvenItemStockDTO> invenItemStockDTOs = responseMessage.get();
        //     List<Integer> prodVariantIds = invenItemStockDTOs.stream()
        //             .map(InvenItemStockDTO::getProdVariantId)
        //             .collect(Collectors.toList());
        //     List<InventoryItem> inventoryItems = inventoryItemRepository.findByProdVariantIds(prodVariantIds);
        //     List<StockMovementRequest> stockMovementRequests = invenItemStockDTOs.stream()
        //             .map((InvenItemStockDTO invenItemStockDTO) -> {
        //                 InventoryItem inventoryItem = inventoryItems.stream()
        //                         .filter(item -> item.getProdVariantId().equals(invenItemStockDTO.getProdVariantId()))
        //                         .findFirst()
        //                         .orElse(null);
        //                 if (inventoryItem != null) {
        //                     return StockMovementRequest.builder()
        //                             .inventoryItemId(inventoryItem.getId())
        //                             .orderId(invenItemStockDTO.getOrderId())
        //                             .ordersCode(invenItemStockDTO.getOrderCode())
        //                             .movementQuantity(invenItemStockDTO.getItemQuantity())
        //                             .movementQuantity(invenItemStockDTO.getItemQuantity())
        //                             .movementStatus(MovementEnum.PENDING.name())
        //                             .build();
        //                 } else {
        //                     return null;
        //                 }
        //             })
        //             .filter(stockMovementRequest -> stockMovementRequest != null)
        //             .collect(Collectors.toList());
        //     if (!stockMovementRequests.isEmpty()) {
        //         rabbitService.sendMessage(myExchange, "create-stock-mvm", stockMovementRequests);
        //     } else {
        //         logger.warn("Invalid message format received in inventory-item-stock:queue");
        //     }
        // } else {
        //     logger.warn("Invalid message format received in inventory-item-stock:queue");
        // }
    }

    @Override
    public Map<Integer, Boolean> onInventoryItemCheckListener(List<InvenItemCheckStock> checkStock) {
        List<InventoryItem> inventoryItems = inventoryItemRepository.findByProdVariantIds(checkStock.stream()
                .map(InvenItemCheckStock::getProdVariantId)
                .collect(Collectors.toList()));

        Map<Integer, Boolean> result = checkStock.stream()
                .collect(Collectors.toMap(
                        InvenItemCheckStock::getProdVariantId,
                        check -> inventoryItems.stream()
                                .filter(item -> item.getProdVariantId().equals(check.getProdVariantId()))
                                .anyMatch(item -> item.getItemQuantity() > check.getItemQuantity())
                ));

        return result;
    }

    @Override
    @Transactional
    public List<InventoryItem> createInventoryItem(List<InventoryItemRequest> requests) {

        if (requests == null || requests.isEmpty()) {
            throw new IllegalArgumentException("List of requests cannot be null or empty");
        }

        List<InventoryItem> inventoryItems = new ArrayList<>();

        for (InventoryItemRequest request : requests) {
            InventoryItem inventoryItem = inventoryItemMapper.toInInventoryItem(request);

            inventoryItems.add(inventoryItem);
        }

        inventoryItemRepository.saveAll(inventoryItems);

        eventPublisher.publishEvent(inventoryItems);

        return inventoryItems;
    }

    @Override
    @Transactional
    public InventoryItem updateInventoryItem(InventoryItemRequest request) {
        InventoryItem existInventoryItem = inventoryItemRepository.findById(request.getId()).orElseThrow(()
                -> new EntityNotFoundException("InventoryItem not found"));

        inventoryItemMapper.updateInventoryItemFromRequest(request, existInventoryItem);

        inventoryItemRepository.save(existInventoryItem);

        eventPublisher.publishEvent(existInventoryItem);

        return existInventoryItem;
    }

    @Override
    @Transactional
    public Boolean deleteInventoryItem(UUID id) {
        InventoryItem existInventoryItem = inventoryItemRepository.findById(id).orElseThrow(()
                -> new EntityNotFoundException("InventoryItem not found"));

        inventoryItemRepository.delete(existInventoryItem);

        eventPublisher.publishEvent(existInventoryItem);

        return true;
    }

    @Override
    @Transactional
    public InventoryItem inventoryIntake(UUID id, int quantity) {
        InventoryItem inventoryItem = inventoryItemRepository.findById(id).orElseThrow(()
                -> new EntityNotFoundException("InventoryItem not found"));

        if (!inventoryRepository.existsByIdAndIsAllowed(inventoryItem.getInventory().getId(), true)) {
            throw new EntityNotFoundException("Not Permision");
        }

        int itemQuantity = inventoryItem.getItemQuantity() + quantity;

        inventoryItem.setItemQuantity(itemQuantity);

        inventoryItemRepository.save(inventoryItem);

        eventPublisher.publishEvent(inventoryItem);

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
