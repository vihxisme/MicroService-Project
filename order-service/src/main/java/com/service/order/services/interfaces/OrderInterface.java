package com.service.order.services.interfaces;

import java.util.UUID;

import com.service.order.entities.Order;
import com.service.order.requests.OrderRequest;

public interface OrderInterface {

    Order createOrder(OrderRequest request);

    Order updateOrder(OrderRequest request);

    Boolean deleteOrder(UUID id);
}
