package com.service.product.listeners;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import com.service.product.entities.Color;

import jakarta.persistence.PostPersist;
import jakarta.persistence.PostRemove;
import jakarta.persistence.PostUpdate;

public class ColorListener {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @PostPersist
    @PostUpdate
    @PostRemove
    public void afterChange(Color color) {
        rabbitTemplate.convertAndSend("cache-update-exchange", "clear-cache:queue", "*");
    }
}
