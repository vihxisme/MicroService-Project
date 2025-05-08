package com.service.order.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.service.order.entities.OrderItem;
import com.service.order.resources.OrderItemResource;

import feign.Param;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {

    @Query("""
      SELECT new com.service.order.resources.OrderItemResource(
        oi.id,
        oi.productId,
        oi.prodVariantId,
        oi.quantity,
        oi.price,
        oi.totalPrice
      )
      FROM OrderItem oi
      WHERE oi.orders.id = :orderId
      """)
    List<OrderItemResource> findOrderItemByOrderId(@Param("orderId") UUID orderId);
}
