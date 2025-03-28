package com.service.inventory.listeners;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.service.events.dto.UpdateProductStatusDTO;
import com.service.inventory.entities.Inventory;

import jakarta.persistence.PostPersist;
import jakarta.persistence.PostRemove;
import jakarta.persistence.PostUpdate;

public class InventoryListener {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.product.exchange}")
    private String productExchange;

    @PostPersist
    @PostUpdate
    @PostRemove
    public void afterChange(Inventory inventory) {
        if (inventory != null) {
            rabbitTemplate.convertAndSend("cache-update-exchange", "clear-cache", "inven");

            if (inventory.getQuantity() != null && inventory.getIsAllowed() != false) {
                String status = inventory.getQuantity() != 0 ? "ACTIVE" : "OUT_OF_STOCK";

                rabbitTemplate.convertAndSend(
                        productExchange,
                        "update-product-status",
                        UpdateProductStatusDTO.builder()
                                .productId(inventory.getId())
                                .status(status)
                                .build());
            } else {
                String status = "INACTIVE";
                rabbitTemplate.convertAndSend(
                        productExchange,
                        "update-product-status",
                        UpdateProductStatusDTO.builder()
                                .productId(inventory.getId())
                                .status(status)
                                .build());
            }
        }
    }
}
