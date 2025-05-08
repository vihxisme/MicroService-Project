package com.service.discount.repositories;

import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "api-composition")
public interface ApiClient {

    @GetMapping("/discount-client/target")
    ResponseEntity<?> getDiscountTargetWithTargetIdName(@RequestParam int page, @RequestParam int size);

    @GetMapping("/discount-client/target-by")
    ResponseEntity<?> getDiscountTargetWithTargetIdName(@RequestParam UUID discountId, @RequestParam String targetType, @RequestParam int page, @RequestParam int size);
}
