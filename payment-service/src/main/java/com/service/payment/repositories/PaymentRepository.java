package com.service.payment.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.service.payment.entities.Payment;

import feign.Param;

public interface PaymentRepository extends JpaRepository<Payment, UUID> {

    boolean existsByPaymentCode(String code);

    @Query("""
        SELECT p.paymentStatus, COUNT(p)
        FROM Payment p
        GROUP BY p.paymentStatus
    """)
    List<Object[]> countByPaymentStatus();

    @Query("""
        SELECT p FROM Payment p
        WHERE p.orderId = :orderId AND p.orderCode = :orderCode
    """)
    Payment findByOrderIdAndOrderCode(@Param("orderId") UUID orderId, @Param("orderCode") String orderCode);

    Payment findByPaymentCode(String paymentCode);

    Payment findByOrderId(UUID orderId);
}
