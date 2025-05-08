package com.service.order.services.interfaces;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.service.order.dtos.OrderDTO;
import com.service.order.entities.Order;
import com.service.order.requests.OrderRequest;
import com.service.order.requests.PaginationRequest;
import com.service.order.resources.OrderDetailResource;
import com.service.order.resources.OrderMnResource;
import com.service.order.resources.OrderStatisticsResource;
import com.service.order.resources.TransactionResource;
import com.service.order.responses.PaginationResponse;
import com.service.order.wrappers.OrderWrapper;

public interface OrderInterface {

    Order create(OrderWrapper wrapper);

    Order createOrder(OrderRequest request);

    Order updateOrder(OrderRequest request);

    Boolean deleteOrder(UUID id);

    Object submitOrder(OrderWrapper wrapper);

    PaginationResponse<OrderMnResource> getAllOrder(PaginationRequest request);

    OrderStatisticsResource getOrderStatistics();

    Map<UUID, TransactionResource> caculateTransaction(List<UUID> userIds);

    OrderDetailResource getOrderById(UUID id);

    Map<UUID, BigDecimal> getTopProductByRevenue(Integer limit);

    OrderRequest testRabbitMq(List<OrderRequest> request);

    String bodySendEmail();
}
