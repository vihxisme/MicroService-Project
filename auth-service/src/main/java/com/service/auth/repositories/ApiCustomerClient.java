package com.service.auth.repositories;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.service.auth.requests.AddInfoCustomerRequest;

@FeignClient(name = "api-composition")
public interface ApiCustomerClient {
  @PostMapping("/customer-client/proxy/internal/profile/info/add")
  ResponseEntity<?> addInfoCustomer(@RequestBody AddInfoCustomerRequest request);

  @GetMapping("/customer-client/proxy/internal/profile/info/{authId}")
  ResponseEntity<?> getUserIdByAuthId(@PathVariable String authId);
}
