package com.service.order.repositories;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.service.order.dtos.OrderRevenueStatsDTO;
import com.service.order.dtos.OrderStatusCountInterfaceDTO;
import com.service.order.dtos.OrderStatusStatsDTO;
import com.service.order.dtos.RevenueByRangeTypeDTO;
import com.service.order.dtos.RevenueStatsDTO;
import com.service.order.dtos.TopProductDTO;
import com.service.order.entities.Order;
import com.service.order.resources.OrderMnResource;

import feign.Param;

@Repository
public interface StatisticsRepository extends JpaRepository<Order, UUID> {

    @Query("""
      SELECT SUM(CASE WHEN o.status = 4 THEN o.totalAmount ELSE 0 END) 
            FROM Order o
      """)
    BigDecimal totalRevenue();

    @Query("""
      SELECT SUM(CASE WHEN o.status = 4 THEN oi.totalPrice ELSE 0 END) 
            FROM Order o
            JOIN o.orderItem oi
            WHERE o.userId IS NOT NULL
      """)
    BigDecimal totalRevenueByUserId();

    @Query("""
        SELECT COUNT(o.id)
        FROM Order o
        WHERE o.userId IS NULL
        """)
    Long countCustomer();

    @Query("""
        SELECT COUNT(o.id)
        FROM Order o
        WHERE o.status = 1
        """)
    Long countNewOrder();

    @Query(value = """
        SELECT 
            :label AS label,
            COALESCE(SUM(total_amount), 0) AS totalRevenue,
            COUNT(*) AS orderCount,
            CASE 
                WHEN :prevRevenue = 0 THEN 0
                ELSE ROUND(((SUM(total_amount) - :prevRevenue) / :prevRevenue) * 100, 2)
            END AS growthPercent
        FROM orders 
        WHERE created_at BETWEEN :start AND :end AND status = 4
    """, nativeQuery = true)
    RevenueStatsDTO getStatsBetween(
            @Param("label") String label,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            @Param("prevRevenue") BigDecimal prevRevenue
    );

    @Query("SELECT COALESCE(SUM(o.totalAmount), 0) FROM Order o WHERE o.createdAt BETWEEN :start AND :end AND status = 4")
    BigDecimal sumTotalRevenue(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query(value = """
        SELECT status, COUNT(*) AS count
        FROM orders
        WHERE created_at BETWEEN :start AND :end
        GROUP BY status
    """, nativeQuery = true)
    List<OrderStatusCountInterfaceDTO> countOrdersByStatusBetween(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );

    @Query(value = """
        SELECT DATE_FORMAT(created_at, '%H:%i') AS label, SUM(total_amount) AS revenue
        FROM orders
        WHERE created_at >= NOW() - INTERVAL 24 HOUR AND status = 4
        GROUP BY DATE_FORMAT(created_at, '%H:%i')
        ORDER BY label ASC
    """, nativeQuery = true)
    List<RevenueByRangeTypeDTO> getRevenueByHour();

    @Query(value = """
    SELECT DATE(created_at) AS label, SUM(total_amount) AS revenue
        FROM orders
        WHERE created_at >= CURDATE() - INTERVAL 7 DAY AND status = 4
        GROUP BY DATE(created_at)
        ORDER BY label ASC
    """, nativeQuery = true)
    List<RevenueByRangeTypeDTO> getRevenueByDay();

    @Query(value = """
        SELECT DATE_FORMAT(created_at, '%Y-%m') AS label, SUM(total_amount) AS revenue
        FROM orders
        WHERE created_at >= NOW() - INTERVAL 12 MONTH AND status = 4
        GROUP BY DATE_FORMAT(created_at, '%Y-%m')
        ORDER BY label ASC
    """, nativeQuery = true)
    List<RevenueByRangeTypeDTO> getRevenueByMonth();

    @Query(value = """
        SELECT 
            CONCAT('Q', qtr, '/', yr) AS label, 
            SUM(total_amount) AS revenue
        FROM (
            SELECT 
                QUARTER(created_at) AS qtr, 
                YEAR(created_at) AS yr, 
                total_amount
            FROM orders
            WHERE created_at >= NOW() - INTERVAL 4 QUARTER AND status = 4
        ) AS sub
        GROUP BY qtr, yr
        ORDER BY yr, qtr
    """, nativeQuery = true)
    List<RevenueByRangeTypeDTO> getRevenueByQuarter();

    @Query(value = """
        SELECT YEAR(created_at) AS label, SUM(total_amount) AS revenue
        FROM orders
        WHERE status = 4
        GROUP BY YEAR(created_at)
        ORDER BY YEAR(created_at) ASC
    """, nativeQuery = true)
    List<RevenueByRangeTypeDTO> getRevenueByYear();

    @Query(value = """
        SELECT 
            COALESCE(SUM(total_amount), 0) AS totalRevenue
        FROM orders
        WHERE created_at BETWEEN :start AND :end AND status = 4
    """, nativeQuery = true)
    OrderRevenueStatsDTO getStatsRevenueBetween(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );

    @Query(value = """
        SELECT 
            COUNT(*) AS totalOrder
        FROM orders
        WHERE created_at BETWEEN :start AND :end
    """, nativeQuery = true)
    OrderStatusStatsDTO getStatsOrderBetween(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );

    @Query(value = """
        SELECT 
        COALESCE(SUM(revenue), 0) AS totalRevenue
        FROM (
            SELECT oi.product_id, SUM(oi.total_price) AS revenue
            FROM orders o
            JOIN order_items oi ON oi.order_id = o.id
            WHERE oi.created_at BETWEEN :start AND :end
            AND o.status = 4
            GROUP BY oi.product_id
            ORDER BY revenue DESC
            LIMIT 10
        ) top_products
    """, nativeQuery = true)
    OrderRevenueStatsDTO findTopProductRevenueBetween(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );

    @Query(value = """
        SELECT COUNT(o.id) AS totalOrder
        FROM orders o
        WHERE o.user_id IS NULL AND created_at BETWEEN :start AND :end
    """, nativeQuery = true)
    OrderStatusStatsDTO getStatsNewCustomerBetween(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );

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
        WHERE o.createdAt BETWEEN :start AND :end
        ORDER BY o.createdAt desc
            """)
    Page<OrderMnResource> findStatsOrder(Pageable pageable,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end);

    @Query("""
            SELECT
                oi.productId AS productId,
                SUM(oi.totalPrice) AS totalRevenue
            FROM Order o
            JOIN o.orderItem oi
            WHERE o.status = 4 AND o.createdAt BETWEEN :start AND :end
            GROUP BY oi.productId
            ORDER BY totalRevenue DESC
            """)
    List<TopProductDTO> findTopProductByRangeType(Pageable pageable,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end);
}
