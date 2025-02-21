package com.service.customer.services.impl;

import java.util.Optional;
import java.util.Random;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.PageRequest;

import com.service.customer.entities.Customer;
import com.service.customer.mappers.CustomerMapper;
import com.service.customer.repositories.CustomerRepository;
import com.service.customer.requests.AddInfoCustomerRequest;
import com.service.customer.requests.AvatarRequest;
import com.service.customer.requests.PaginationRequest;
import com.service.customer.requests.UpdateInfoCustomerRequest;
import com.service.customer.resources.AvatarResource;
import com.service.customer.resources.CustomerProfileResource;
import com.service.customer.responses.PaginationResponse;
import com.service.customer.services.interfaces.CustomerInterface;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class CustomerService implements CustomerInterface {

  private Logger logger = LoggerFactory.getLogger(CustomerService.class);

  @Autowired
  private CustomerRepository customerRepository;

  @Autowired
  private CustomerMapper customerMapper;

  @Override
  @Transactional
  public Customer updateInfoCustomer(UpdateInfoCustomerRequest request) {

    Optional<Customer> existingCustomerOptional = customerRepository
        .findByAuthUserId(request.getAuthUserId());

    if (existingCustomerOptional.isPresent()) {
      Customer existingCustomer = existingCustomerOptional.get();

      customerMapper.updateCustomerFromDto(request, existingCustomer);

      return customerRepository.save(existingCustomer);
    } else {
      throw new EntityNotFoundException(
          "Customer with authUserId " + request.getAuthUserId() + " not found");
    }
  }

  public String generateCustomerCode(String prefix) {
    Boolean isUnique;
    Random random = new Random();
    String code;
    do {
      int randomNumberFive = random.nextInt(10000, 99999);
      int randomNumberThree = random.nextInt(100, 999);
      code = String.format("%s-%05d-%03d", prefix, randomNumberFive, randomNumberThree);
      isUnique = !customerRepository.existsByCustomerCode(code);
    } while (!isUnique);

    return code;
  }

  @Override
  @Transactional
  public AvatarResource updateAvatar(AvatarRequest request) {
    Boolean updAvatar = customerRepository.updateAvatar(request.getId(), request.getAvatar()) > 0;

    return new AvatarResource(updAvatar);
  }

  @Override
  @Transactional
  public Customer addInfoCustomer(AddInfoCustomerRequest request) {

    Optional<Customer> existingCustomerOptional = customerRepository
        .findByAuthUserId(request.getAuthUserId());

    if (!existingCustomerOptional.isPresent()) {

      String customerCode = generateCustomerCode("PGX");

      Customer customer = customerMapper.toCustomer(request);
      customer.setCustomerCode(customerCode);

      return customerRepository.save(customer);
    } else {
      throw new EntityNotFoundException(
          "Customer with authUserId " + request.getAuthUserId() + " already exists");
    }
  }

  @Override
  public Customer getCustomer(UUID id) {
    Optional<Customer> customer = customerRepository.findById(id);

    if (!customer.isPresent())
      throw new EntityNotFoundException("Customer with id " + customer.get().getId() + " not found");

    return customer.get();
  }

  @Override
  public UUID getIdByAuthUserId(UUID authUserId) {
    Optional<UUID> id = customerRepository.findIdByAuthUserId(authUserId);

    if (id.isPresent())
      return id.get();
    else
      throw new EntityNotFoundException("Customer not found");
  }

  @Override
  public PaginationResponse<CustomerProfileResource> getAllCustomer(PaginationRequest request) {
    Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
    Page<Customer> page = customerRepository.findAll(pageable);

    Page<CustomerProfileResource> customerProfilePage = page.map(customerMapper::toProfileResource);

    return PaginationResponse.<CustomerProfileResource>builder()
        .content(customerProfilePage.getContent())
        .pageNumber(customerProfilePage.getNumber())
        .pageSize(customerProfilePage.getSize())
        .totalPages(customerProfilePage.getTotalPages())
        .totalElements(customerProfilePage.getTotalElements())
        .build();

  }

}
