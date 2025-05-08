package com.service.order.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import com.service.order.entities.OrderItem;
import com.service.order.listener.events.OrderItemListChangedEvent;
import com.service.order.mappers.OrderItemMapper;
import com.service.order.repositories.OrderItemRepository;
import com.service.order.requests.OrderItemRequest;
import com.service.order.resources.OrderItemResource;
import com.service.order.services.interfaces.OrderItemInterface;
import com.service.order.wrappers.OrderItemReqWrapper;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class OrderItemService implements OrderItemInterface {

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    private Logger logger = LoggerFactory.getLogger(OrderItemService.class);

    @RabbitListener(queues = "create-order-item:queue")
    @Transactional
    public void createOrderItemListener(OrderItemReqWrapper wrapper) {
        createOrderItems(wrapper.getOrderItemRequestList());
    }

    @Override
    @Transactional
    public List<OrderItem> createOrderItems(List<OrderItemRequest> requests) {
        if (requests == null || requests.isEmpty()) {
            throw new IllegalArgumentException("List of requests cannot be null or empty");
        }

        logger.info("Size: {}", requests.size());

        List<OrderItem> orderItems = new ArrayList<>();

        for (OrderItemRequest request : requests) {
            OrderItem orderItem = orderItemMapper.toOrderItem(request);
            orderItems.add(orderItem);
        }

        orderItemRepository.saveAll(orderItems);
        eventPublisher.publishEvent(OrderItemListChangedEvent.builder().orderItems(orderItems).build());

        return orderItems;
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

    @Override
    public List<OrderItemResource> getOrderITemByOrderId(UUID orderId) {
        List<OrderItemResource> orderItems = orderItemRepository.findOrderItemByOrderId(orderId);

        if (orderItems == null || orderItems.isEmpty()) {
            throw new EntityNotFoundException("Not found");
        }

        logger.info("size: {}", orderItems.size());
        return orderItems;
    }

}
