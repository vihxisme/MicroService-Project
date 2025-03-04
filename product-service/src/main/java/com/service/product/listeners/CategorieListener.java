package com.service.product.listeners;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import com.service.product.entities.Categorie;

import jakarta.persistence.PostPersist;
import jakarta.persistence.PostRemove;
import jakarta.persistence.PostUpdate;

public class CategorieListener {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @PostPersist
    @PostUpdate
    @PostRemove
    public void afterChange(Categorie categorie) {
        rabbitTemplate.convertAndSend("cache-update-exchange", "clear-cache:queue", "*");
    }
}
