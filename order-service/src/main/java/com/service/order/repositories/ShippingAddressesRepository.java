package com.service.order.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.service.order.entities.ShippingAddresses;

@Repository
public interface ShippingAddressesRepository extends JpaRepository<ShippingAddresses, Integer> {

}
