package com.service.inventory.listeners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.service.events.dto.UpdateVariantQuantityDTO;
import com.service.inventory.entities.Inventory;
import com.service.inventory.entities.InventoryItem;
import com.service.inventory.repositories.InventoryItemRepository;
import com.service.inventory.repositories.InventoryRepository;

import jakarta.persistence.PostPersist;
import jakarta.persistence.PostRemove;
import jakarta.persistence.PostUpdate;

public class InventoryItemListener {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private InventoryItemRepository inventoryItemRepository;

    private Logger logger = LoggerFactory.getLogger(InventoryItemListener.class);

    @Value("${rabbitmq.exchange.product}")
    private String productExchange;

    @PostPersist
    @PostUpdate
    @PostRemove
    public void afterChange(InventoryItem inventoryItem) {

        if (inventoryItem != null) {
            Inventory inventory = inventoryRepository.findById(inventoryItem.getInventory().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Inventory not found for ID: " + inventoryItem.getInventory().getId()));

            Integer totalQuantity = inventoryItemRepository.sumItemQuantityByInventoryId(inventory.getId());
            int updatedQuantity = totalQuantity != null ? Math.max(totalQuantity, 0) : 0;
            inventoryRepository.save(inventory);
            inventory.setQuantity(updatedQuantity);

            rabbitTemplate.convertAndSend("cache-update-exchange", "clear-cache", "inven");

            if (inventoryItem.getItemQuantity() != null) {
                rabbitTemplate.convertAndSend(
                        productExchange,
                        "update-variant-quantity",
                        UpdateVariantQuantityDTO.builder()
                                .prodVariantId(inventoryItem.getProdVariantId())
                                .quantity(inventoryItem.getItemQuantity())
                                .build());
            }
        }
    }
}
