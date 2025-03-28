package com.service.inventory.listeners;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.service.events.dto.UpdateVariantQuantityDTO;
import com.service.inventory.entities.Inventory;
import com.service.inventory.entities.InventoryItem;
import com.service.inventory.repositories.InventoryItemRepository;
import com.service.inventory.repositories.InventoryRepository;

@Component
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

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onInventoryItemChanged(InventoryItem item) {
        if (item == null) {
            return;
        }

        updateInventoryQuantityAndNotify(item.getInventory().getId());

        // Notify variant quantity
        if (item.getItemQuantity() != null) {
            rabbitTemplate.convertAndSend(
                    productExchange,
                    "update-variant-quantity",
                    UpdateVariantQuantityDTO.builder()
                            .prodVariantId(item.getProdVariantId())
                            .quantity(item.getItemQuantity())
                            .build()
            );
        }
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onInventoryItemListChanged(List<InventoryItem> items) {
        if (items == null || items.isEmpty()) {
            return;
        }

        Set<UUID> inventoryIds = items.stream()
                .map(i -> i.getInventory().getId())
                .collect(Collectors.toSet());

        inventoryIds.forEach(this::updateInventoryQuantityAndNotify);

        items.forEach(item -> {
            if (item.getItemQuantity() != null) {
                rabbitTemplate.convertAndSend(
                        productExchange,
                        "update-variant-quantity",
                        UpdateVariantQuantityDTO.builder()
                                .prodVariantId(item.getProdVariantId())
                                .quantity(item.getItemQuantity())
                                .build()
                );
            }
        });
    }

    private void updateInventoryQuantityAndNotify(UUID inventoryId) {
        Inventory inventory = inventoryRepository.findById(inventoryId)
                .orElseThrow(() -> new IllegalArgumentException("Inventory not found for ID: " + inventoryId));

        Integer totalQuantity = inventoryItemRepository.sumItemQuantityByInventoryId(inventoryId);
        int updatedQuantity = totalQuantity != null ? Math.max(totalQuantity, 0) : 0;

        inventory.setQuantity(updatedQuantity);
        inventoryRepository.save(inventory);

        rabbitTemplate.convertAndSend("cache-update-exchange", "clear-cache", "inven");
    }
}
