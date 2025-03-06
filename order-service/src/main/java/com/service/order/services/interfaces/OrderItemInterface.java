package com.service.order.services.interfaces;

import java.util.List;

import com.service.order.entities.OrderItem;
import com.service.order.requests.OrderItemRequest;

public interface OrderItemInterface {

    List<OrderItem> createOrderItems(List<OrderItemRequest> requests);

    List<OrderItem> updateOrderItems(List<OrderItemRequest> requests);

    Boolean deleteOrderItems(List<Integer> ids);
}
