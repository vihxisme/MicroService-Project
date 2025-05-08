package com.service.customer.repositories;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "api-composition")
public interface ApiClient {

    @GetMapping("/customer-client/info/with-transaction")
    public ResponseEntity<?> getCustomerTransaction(@RequestParam int page, @RequestParam int size);

    @GetMapping("/customer-client/new-customer/with-transaction")
    public ResponseEntity<?> getNewCustomerWithTransactionByRangeType(@RequestParam Integer limit, @RequestParam String rangeType);
}
