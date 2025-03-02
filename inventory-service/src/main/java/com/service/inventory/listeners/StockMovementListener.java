package com.service.inventory.listeners;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import com.service.inventory.entities.StockMovement;

import jakarta.persistence.PostPersist;
import jakarta.persistence.PostRemove;
import jakarta.persistence.PostUpdate;

public class StockMovementListener {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @PostPersist
    @PostUpdate
    @PostRemove
    public void afterChange(StockMovement stockMovement) {
        rabbitTemplate.convertAndSend("cache-update-exchange", "clear-cache", "inven");
    }
}
