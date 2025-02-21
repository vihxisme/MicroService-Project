package com.service.customer.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.service.customer.entities.Customer;
import com.service.customer.requests.AvatarRequest;
import com.service.customer.requests.PaginationRequest;
import com.service.customer.requests.UpdateInfoCustomerRequest;
import com.service.customer.resources.AvatarResource;
import com.service.customer.resources.CustomerProfileResource;
import com.service.customer.responses.PaginationResponse;
import com.service.customer.responses.SuccessResponse;
import com.service.customer.services.interfaces.CustomerInterface;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/profile")
public class CustomerController {

  @Autowired
  private CustomerInterface customerInterface;

  @PutMapping("/info/update")
  public ResponseEntity<?> updateInfoCustomer(@Valid @RequestBody UpdateInfoCustomerRequest request) {
    Customer customer = customerInterface.updateInfoCustomer(request);

    SuccessResponse<Customer> success = new SuccessResponse<>("SUCCESS", customer);
    return ResponseEntity.ok(success);
  }

  @PutMapping("/info/update-avatar")
  public ResponseEntity<?> updateAvatar(@Valid @RequestBody AvatarRequest request) {
    AvatarResource resource = customerInterface.updateAvatar(request);

    SuccessResponse<AvatarResource> success = new SuccessResponse<>("SUCCESS", resource);
    return ResponseEntity.ok(success);
  }

  @GetMapping("/info/{id}")
  public ResponseEntity<?> getCustomer(@PathVariable String id) {
    Customer customer = customerInterface.getCustomer(UUID.fromString(id));

    SuccessResponse<Customer> success = new SuccessResponse<>("SUCCESS", customer);
    return ResponseEntity.ok(success);
  }

  @GetMapping("/info/all")
  public ResponseEntity<?> getAllCustomer(@ModelAttribute PaginationRequest request) {
    PaginationResponse<CustomerProfileResource> response = customerInterface.getAllCustomer(request);

    SuccessResponse<PaginationResponse<CustomerProfileResource>> success = new SuccessResponse<>("SUCCESS", response);
    return ResponseEntity.ok(success);
  }
}
