package com.service.customer.services.impl;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.service.customer.entities.Customer;
import com.service.customer.enums.GenderEnum;
import com.service.customer.repositories.CustomerRepository;
import com.service.customer.requests.AddInfoCustomerRequest;
import com.service.customer.services.interfaces.CustomerInterface;

import lombok.Builder;

@Service
@Builder
public class CustomerService implements CustomerInterface {

  @Autowired
  private CustomerRepository customerRepository;

  @Override
  public Object addInfoCustomer(AddInfoCustomerRequest addInfoCustomerRequest) {
    String customerCode = generateCustomerCode("PGX");

    Customer customer = Customer.builder()
        .customerCode(customerCode)
        .authUserId(addInfoCustomerRequest.getAuthUserId())
        .firstName(addInfoCustomerRequest.getFirstName())
        .lastName(addInfoCustomerRequest.getLastName())
        .dateOfBirth(addInfoCustomerRequest.getDob())
        .gender(GenderEnum.valueOf(addInfoCustomerRequest.getGender()))
        .avatar(addInfoCustomerRequest.getAvatar())
        .build();

    return customerRepository.save(customer);
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

}
