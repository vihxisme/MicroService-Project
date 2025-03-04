package com.service.cart.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.service.cart.entities.CartItem;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Integer> {

}
