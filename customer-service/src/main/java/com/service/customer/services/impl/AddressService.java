package com.service.customer.services.impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.service.customer.entities.Address;
import com.service.customer.entities.Customer;
import com.service.customer.mappers.AddressMapper;
import com.service.customer.repositories.AddressRepository;
import com.service.customer.repositories.CustomerRepository;
import com.service.customer.requests.AddAddressRequest;
import com.service.customer.requests.UpdateAddrerssRequest;
import com.service.customer.services.interfaces.AddressInterface;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class AddressService implements AddressInterface {

  private Logger logger = LoggerFactory.getLogger(AddressService.class);

  @Autowired
  private AddressRepository addressRepository;

  @Autowired
  private CustomerRepository customerRepository;

  @Autowired
  private AddressMapper addressMapper;

  @Override
  @Transactional
  public Address addAddress(AddAddressRequest request) {
    if (request.getIsDefault()) {
      Customer customer = customerRepository.findById(request.getCustomerID())
          .orElseThrow(() -> new EntityNotFoundException("Customer not found"));

      addressRepository.updateIsDefaultByCustomerID(customer);
    }

    Address address = addressMapper.toAddress(request);

    return addressRepository.save(address);
  }

  @Override
  public List<Address> getAllAddressByCustomer(UUID customerId) {
    Customer customer = customerRepository.findById(customerId)
        .orElseThrow(() -> new EntityNotFoundException("Customer not found"));

    return addressRepository.findByCustomerID(customer);
  }

  @Override
  @Transactional
  public Address updateAddress(UpdateAddrerssRequest request) {
    if (request.getIsDefault()) {
      Customer customer = customerRepository.findById(request.getCustomerID())
          .orElseThrow(() -> new EntityNotFoundException("Customer not found"));

      addressRepository.updateIsDefaultByCustomerID(customer);
    }

    Optional<Address> existingAddressOptional = addressRepository.findById(request.getId());

    if (existingAddressOptional.isPresent()) {
      Address address = existingAddressOptional.get();

      addressMapper.updateAddressFromDto(request, address);

      return addressRepository.save(address);
    } else {
      throw new EntityNotFoundException("Address with Id " + request.getId() + " not found");
    }
  }

  @Override
  @Transactional
  public Boolean deleteAddress(Long id) {
    Boolean isDelete = addressRepository.deleteByIdCustom(id) > 0;
    return isDelete;
  }

}
