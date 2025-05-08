package com.service.discount.Listeners;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import com.service.discount.entities.DiscountTarget;

import jakarta.persistence.PostPersist;
import jakarta.persistence.PostRemove;
import jakarta.persistence.PostUpdate;

public class DiscountTargetListener {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @PostPersist
    @PostUpdate
    @PostRemove
    public void afterChange(DiscountTarget discountTarget) {
        rabbitTemplate.convertAndSend("cache-update.exchange", "clear-cache", "*");
    }
}
