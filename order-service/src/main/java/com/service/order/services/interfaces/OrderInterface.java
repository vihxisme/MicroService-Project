package com.service.order.services.interfaces;

import java.util.UUID;

import com.service.order.entities.Order;
import com.service.order.requests.OrderRequest;
import com.service.order.wrappers.OrderWrapper;

public interface OrderInterface {

    Order create(OrderWrapper wrapper);

    Order createOrder(OrderRequest request);

    Order updateOrder(OrderRequest request);

    Boolean deleteOrder(UUID id);
}
