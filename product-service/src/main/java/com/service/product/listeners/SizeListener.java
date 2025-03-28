package com.service.product.listeners;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import com.service.product.entities.Size;

import jakarta.persistence.PostPersist;
import jakarta.persistence.PostRemove;
import jakarta.persistence.PostUpdate;

public class SizeListener {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @PostPersist
    @PostUpdate
    @PostRemove
    public void afterChange(Size size) {
        rabbitTemplate.convertAndSend("cache-update-exchange", "clear-cache:queue", "*");
    }
}
