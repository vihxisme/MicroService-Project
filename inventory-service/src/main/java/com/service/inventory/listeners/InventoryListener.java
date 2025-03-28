package com.service.inventory.listeners;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.service.events.dto.UpdateProductStatusDTO;
import com.service.inventory.entities.Inventory;

@Component
public class InventoryListener {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.exchange.product}")
    private String productExchange;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onInventoryChanged(Inventory inventory) {
        // Clear cache
        rabbitTemplate.convertAndSend("cache-update-exchange", "clear-cache", "inven");

        // Determine status
        String status = "INACTIVE";
        if (inventory.getQuantity() != null && Boolean.TRUE.equals(inventory.getIsAllowed())) {
            status = inventory.getQuantity() != 0 ? "ACTIVE" : "OUT_OF_STOCK";
        }

        // Notify product-service
        rabbitTemplate.convertAndSend(
                productExchange,
                "update-product-status",
                UpdateProductStatusDTO.builder()
                        .productId(inventory.getId())
                        .status(status)
                        .build()
        );
    }
}
