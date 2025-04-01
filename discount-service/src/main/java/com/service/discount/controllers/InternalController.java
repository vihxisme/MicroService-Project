package com.service.discount.controllers;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.service.discount.requests.PaginationRequest;
import com.service.discount.services.interfaces.DiscountInterface;

@RestController
@RequestMapping("/internal")
public class InternalController {

    @Autowired
    private DiscountInterface discountInterface;

    @GetMapping("/discount-client/info")
    public ResponseEntity<?> getAllDiscountClient() {
        return ResponseEntity.ok(discountInterface.getAllDiscountsClient());
    }

    @GetMapping("/discount-client/with-target")
    public ResponseEntity<?> getDiscountsWithTarget(@ModelAttribute PaginationRequest request) {
        return ResponseEntity.ok(discountInterface.getDiscountWithTargets(request));
    }

    @GetMapping("/discount-client/by")
    public ResponseEntity<?> getByTargetIdWithDiscountClientResource(@RequestParam UUID targetId) {
        return ResponseEntity.ok(discountInterface.getByTargetIdDiscountClientResource(targetId));
    }
}
