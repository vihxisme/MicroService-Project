package com.service.cart.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.service.cart.entities.Cart;

@Repository
public interface CartRepository extends JpaRepository<Cart, UUID> {

}
