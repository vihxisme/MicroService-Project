package com.service.order.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.service.order.dtos.TopProductDTO;
import com.service.order.entities.Order;
import com.service.order.resources.OrderMnResource;
import com.service.order.resources.OrderStatisticsResource;

import feign.Param;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {

    boolean existsByOrderCode(String code);

    Page<Order> findAll(Pageable pageable);

    @Query("""
        SELECT new com.service.order.resources.OrderMnResource(
            o.id as id,
            o.orderCode as orderCode,
            o.userId as userId,
            o.totalAmount as totalAmount,
            o.status as status,
            o.createdAt as createdAt,
            sa.name as name,
            sa.email as email
        )
        FROM Order o
        JOIN o.shippingAddresses sa
        ORDER BY o.createdAt desc
            """)
    Page<OrderMnResource> findAllOrder(Pageable pageable);

    @Query("""
            SELECT new com.service.order.resources.OrderStatisticsResource(
                COUNT(o.id), 
                SUM(CASE WHEN o.status = 4 THEN o.totalAmount ELSE 0 END), 
                SUM(CASE WHEN o.status = 1 THEN 1 ELSE 0 END),
                SUM(CASE WHEN o.status = 5 THEN 1 ELSE 0 END)
            )
            FROM Order o
            """)
    OrderStatisticsResource getOrdersStatistics();

    @Query("""
        SELECT o FROM Order o WHERE o.status = 4 AND o.userId IN :userIds
            """)
    List<Order> findByUserIds(@Param("userIds") List<UUID> userIds);

    @Query("""
            SELECT
                oi.productId AS productId,
                SUM(oi.totalPrice) AS totalRevenue
            FROM Order o
            JOIN o.orderItem oi
            WHERE o.status = 4
            GROUP BY oi.productId
            ORDER BY totalRevenue DESC
            """)
    List<TopProductDTO> findTopProduct(Pageable pageable);

}
