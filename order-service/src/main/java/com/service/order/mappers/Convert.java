package com.service.order.mappers;

import java.math.BigDecimal;
import java.util.UUID;

import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.service.order.entities.Order;
import com.service.order.repositories.OrderRepository;

import jakarta.persistence.EntityNotFoundException;

@Component
public class Convert {

    @Autowired
    private OrderRepository orderRepository;

    @Named("uuidToOrder")
    public Order uuidToOrder(UUID orderId) {
        return orderRepository.findById(orderId).orElseThrow(()
                -> new EntityNotFoundException("Order not found"));
    }

    @Named("calculateTotalPrice")
    public BigDecimal calculateTotalPrice(BigDecimal price, Integer quantity) {
        return price.multiply(BigDecimal.valueOf(quantity));
    }
}
