package com.service.order.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.service.order.entities.OrderItem;
import com.service.order.mappers.OrderItemMapper;
import com.service.order.repositories.OrderItemRepository;
import com.service.order.requests.OrderItemRequest;
import com.service.order.services.interfaces.OrderItemInterface;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class OrderItemService implements OrderItemInterface {

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Override
    @Transactional
    public List<OrderItem> createOrderItems(List<OrderItemRequest> requests) {
        if (requests == null || requests.isEmpty()) {
            throw new IllegalArgumentException("List of requests cannot be null or empty");
        }

        List<OrderItem> orderItems = new ArrayList<>();

        for (OrderItemRequest request : requests) {
            OrderItem orderItem = orderItemMapper.toOrderItem(request);
            orderItems.add(orderItem);
        }

        return orderItemRepository.saveAll(orderItems);
    }

    @Override
    @Transactional
    public List<OrderItem> updateOrderItems(List<OrderItemRequest> requests) {
        if (requests == null || requests.isEmpty()) {
            throw new IllegalArgumentException("List of requests cannot be null or empty");
        }

        List<OrderItem> orderItems = new ArrayList<>();

        for (OrderItemRequest request : requests) {
            OrderItem existOrderItem = orderItemRepository.findById(request.getId()).orElseThrow(()
                    -> new EntityNotFoundException("OrderItem not found"));
            orderItemMapper.updateOrderItemFromRequest(request, existOrderItem);
            orderItems.add(existOrderItem);
        }

        return orderItemRepository.saveAll(orderItems);
    }

    @Override
    @Transactional
    public Boolean deleteOrderItems(List<Integer> ids) {
        if (ids == null || ids.isEmpty()) {
            throw new IllegalArgumentException("List of requests cannot be null or empty");
        }

        List<OrderItem> orderItems = orderItemRepository.findAllById(ids);

        if (orderItems.size() != ids.size()) {
            throw new IllegalArgumentException("OrderItem not found");
        }

        orderItemRepository.deleteAll(orderItems);

        return true;
    }

}
