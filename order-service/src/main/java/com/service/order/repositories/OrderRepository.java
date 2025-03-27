package com.service.order.repositories;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.service.order.entities.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {

    boolean existsByOrderCode(String code);

    Page<Order> findAll(Pageable pageable);
}
