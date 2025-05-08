package com.service.customer.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
@RequestMapping("/v1/customer")
public class CustomerController {

    @Autowired
    private CustomerInterface customerInterface;

    @PatchMapping("/info/update")
    public ResponseEntity<?> updateInfoCustomer(@Valid @RequestBody UpdateInfoCustomerRequest request) {
        Customer customer = customerInterface.updateInfoCustomer(request);

        SuccessResponse<Customer> success = new SuccessResponse<>("SUCCESS", customer);
        return ResponseEntity.ok(success);
    }

    @PatchMapping("/info/update-avatar")
    public ResponseEntity<?> updateAvatar(@Valid @RequestBody AvatarRequest request) {
        AvatarResource resource = customerInterface.updateAvatar(request);

        SuccessResponse<AvatarResource> success = new SuccessResponse<>("SUCCESS", resource);
        return ResponseEntity.ok(success);
    }

    @GetMapping("/info")
    public ResponseEntity<?> getCustomer(@RequestParam String id) {
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

    @GetMapping("/info-by")
    public ResponseEntity<?> getProfileById(@RequestParam UUID id) {
        return ResponseEntity.ok(new SuccessResponse<>(
                "SUCCESS",
                customerInterface.getProfileById(id)
        ));
    }

    @GetMapping("/with-transaction")
    public ResponseEntity<?> getCustomerTransaction(@ModelAttribute PaginationRequest request) {
        return ResponseEntity.ok(new SuccessResponse<>(
                "SUCCESS",
                customerInterface.getCustomerTransaction(request)));
    }

    @GetMapping("/count/customer")
    public ResponseEntity<?> countCustomer() {
        return ResponseEntity.ok(new SuccessResponse<>(
                "SUCCESS",
                customerInterface.countCustomer()));
    }

    @GetMapping("/statistics/new-customer")
    public ResponseEntity<?> countNewCustomer(@RequestParam String rangeType) {
        return ResponseEntity.ok(new SuccessResponse<>(
                "SUCCESS",
                customerInterface.getStatsNewCustomerByRangeType(rangeType)));
    }

    @GetMapping("/statistics/new-customer/by")
    public ResponseEntity<?> getNewCustomerWithTransaction(@RequestParam Integer limit, @RequestParam String rangeType) {
        return ResponseEntity.ok(new SuccessResponse<>(
                "SUCCESS",
                customerInterface.getNewCustomerWTransactionStatsByRangeType(limit, rangeType)));
    }
}
