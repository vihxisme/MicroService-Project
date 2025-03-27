package com.service.order.services.impl;

import java.util.Random;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.service.order.entities.Order;
import com.service.order.mappers.OrderMapper;
import com.service.order.repositories.OrderRepository;
import com.service.order.requests.OrderRequest;
import com.service.order.services.interfaces.OrderInterface;
import com.service.order.wrappers.OrderWrapper;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class OrderService implements OrderInterface {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderMapper orderMapper;

    private String generateOrderCode() {
        Boolean isUnique;
        Random random = new Random();
        String code;
        do {
            int randomNumberStart = random.nextInt(100, 999);
            int randomNumberMiddle = random.nextInt(10000, 99999);
            int randomNumberEnd = random.nextInt(1000, 9999);
            code = String.format("%03d-%05d-%04d", randomNumberStart, randomNumberMiddle, randomNumberEnd);
            isUnique = !orderRepository.existsByOrderCode(code);
        } while (!isUnique);

        return code;
    }

    @Override
    @Transactional
    public Order createOrder(OrderRequest request) {
        if (request.getOrderCode() == null) {
            request.setOrderCode(generateOrderCode());
        }

        Order order = orderMapper.toOrder(request);

        return orderRepository.save(order);
    }

    @Override
    @Transactional
    public Order updateOrder(OrderRequest request) {
        Order existOrder = orderRepository.findById(request.getId()).orElseThrow(()
                -> new EntityNotFoundException("Order not found"));

        orderMapper.updateOrderFromRequest(request, existOrder);

        return existOrder;
    }

    @Override
    @Transactional
    public Boolean deleteOrder(UUID id) {
        Order existOrder = orderRepository.findById(id).orElseThrow(()
                -> new EntityNotFoundException("Order not found"));

        orderRepository.delete(existOrder);

        return true;
    }

    @Override
    public Order create(OrderWrapper wrapper) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'create'");
    }

}
