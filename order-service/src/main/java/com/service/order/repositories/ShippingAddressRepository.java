package com.service.order.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.service.order.entities.ShippingAddress;

@Repository
public interface ShippingAddressRepository extends JpaRepository<ShippingAddress, Integer> {

}
