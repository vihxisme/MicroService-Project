package com.service.customer.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.service.customer.entities.Address;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

}
