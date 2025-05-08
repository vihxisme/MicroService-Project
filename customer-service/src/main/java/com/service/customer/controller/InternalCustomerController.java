package com.service.customer.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.service.customer.entities.Customer;
import com.service.customer.requests.AddInfoCustomerRequest;
import com.service.customer.requests.PaginationRequest;
import com.service.customer.resources.CustomerProfileResource;
import com.service.customer.responses.PaginationResponse;
import com.service.customer.responses.SuccessResponse;
import com.service.customer.services.interfaces.CustomerInterface;

@RestController
@RequestMapping("/internal/profile")
public class InternalCustomerController {

    @Autowired
    private CustomerInterface customerInterface;

    @PostMapping("/info/add")
    public ResponseEntity<?> addInfoRequest(@RequestBody AddInfoCustomerRequest addInfoCustomerRequest) {
        Customer customer = customerInterface.addInfoCustomer(addInfoCustomerRequest);

        SuccessResponse<Customer> success = new SuccessResponse<>("SUCCESS", customer);
        return ResponseEntity.ok(success);
    }

    @GetMapping("/info/{authUserId}")
    public ResponseEntity<?> getIdByAuthUserId(@PathVariable String authUserId) {
        UUID id = customerInterface.getIdByAuthUserId(UUID.fromString(authUserId));

        SuccessResponse<UUID> success = new SuccessResponse<>("SUCCESS", id);
        return ResponseEntity.ok(success);
    }

    @GetMapping("/info/all")
    public ResponseEntity<?> getAllCustomer(@ModelAttribute PaginationRequest request) {
        PaginationResponse<CustomerProfileResource> response = customerInterface.getAllCustomer(request);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/new-customer/by-rangetype")
    public ResponseEntity<?> getNewCustomerByRangeType(@RequestParam Integer limit, @RequestParam String rangeType) {
        List<CustomerProfileResource> response = customerInterface.getNewCustomerStatsByRangeType(limit, rangeType);

        return ResponseEntity.ok(response);
    }
}
