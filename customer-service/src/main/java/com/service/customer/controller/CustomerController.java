package com.service.customer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.service.customer.entities.Customer;
import com.service.customer.requests.AddInfoCustomerRequest;
import com.service.customer.responses.ErrorResponse;
import com.service.customer.responses.SuccessResponse;
import com.service.customer.services.interfaces.CustomerInterface;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/profile")
public class CustomerController {
  @Autowired
  private CustomerInterface customerInterface;

  @PostMapping("/add")
  public ResponseEntity<?> addInfoCustomer(@Valid @RequestBody AddInfoCustomerRequest addInfoCustomerRequest) {
    Object customer = customerInterface.addInfoCustomer(addInfoCustomerRequest);

    if (customer instanceof Customer item) {
      SuccessResponse<Customer> success = new SuccessResponse<>("SUCCESS", item);
      return ResponseEntity.ok(success);
    }

    if (customer instanceof ErrorResponse errorResponse) {
      return ResponseEntity.unprocessableEntity().body(errorResponse);
    }

    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Network Error");
  }
}
