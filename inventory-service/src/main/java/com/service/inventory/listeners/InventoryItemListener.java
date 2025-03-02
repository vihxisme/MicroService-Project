package com.service.inventory.listeners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import com.service.inventory.dtos.UpdateVariantQuantityDTO;
import com.service.inventory.entities.InventoryItem;

import jakarta.persistence.PostPersist;
import jakarta.persistence.PostRemove;
import jakarta.persistence.PostUpdate;

public class InventoryItemListener {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private Logger logger = LoggerFactory.getLogger(InventoryItemListener.class);

    @PostPersist
    @PostUpdate
    @PostRemove
    public void afterChange(InventoryItem inventoryItem) {

        if (inventoryItem != null) {
            rabbitTemplate.convertAndSend("cache-update-exchange", "clear-cache", "inven");

            if (inventoryItem.getItemQuantity() != null) {
                rabbitTemplate.convertAndSend(
                        "update-variant-quantity-exchange",
                        "update-quantity",
                        UpdateVariantQuantityDTO.builder()
                                .prodVariantId(inventoryItem.getProdVariantId())
                                .quantity(inventoryItem.getItemQuantity())
                                .build());
            }
        }
    }

}
