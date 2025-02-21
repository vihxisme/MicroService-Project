package com.service.customer.mappers;

import java.util.UUID;

import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.service.customer.entities.Customer;
import com.service.customer.repositories.CustomerRepository;

import jakarta.persistence.EntityNotFoundException;

@Component
public class CustomerConverter {

  @Autowired
  private CustomerRepository customerRepository;

  @Named("UUIDtoCustomer")
  public Customer UUIDtoCustomer(UUID customerID) {
    return customerRepository.findById(customerID).orElseThrow(() -> new EntityNotFoundException("Customer not found"));
  }
}
