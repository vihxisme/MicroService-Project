package com.service.cart.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.service.cart.entities.CartItem;

import feign.Param;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Integer> {

    @Query("""
      SELECT ct FROM CartItem ct WHERE ct.cart.userId = :userId
      """)
    List<CartItem> findCartItemByUserId(@Param("userId") UUID userId);
}
