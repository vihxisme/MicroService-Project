package com.service.discount.controllers;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.service.discount.requests.DiscountRequest;
import com.service.discount.requests.PaginationRequest;
import com.service.discount.responses.SuccessResponse;
import com.service.discount.services.interfaces.DiscountInterface;

@RestController
@RequestMapping("/v1/discount")
public class DiscountController {

  @Autowired
  private DiscountInterface discountInterface;

  private Logger logger = LoggerFactory.getLogger(DiscountController.class);

  @PostMapping("/create")
  public ResponseEntity<?> createDiscount(@RequestBody DiscountRequest request) {
    return ResponseEntity.ok(new SuccessResponse<>("SUCCESS", discountInterface.createDiscount(request)));
  }

  @PutMapping("/update")
  public ResponseEntity<?> updateDiscount(@RequestBody DiscountRequest request) {
    return ResponseEntity.ok(new SuccessResponse<>("SUCCESS", discountInterface.updateDiscount(request)));
  }

  @DeleteMapping("/delete/{id}")
  public ResponseEntity<?> deleteDiscount(@PathVariable String id) {
    return ResponseEntity.ok(new SuccessResponse<>("SUCCESS", discountInterface.deleteDiscount(UUID.fromString(id))));
  }

  @GetMapping("/info/all")
  public ResponseEntity<?> getAllDiscounts(@ModelAttribute PaginationRequest request) {
    return ResponseEntity.ok(new SuccessResponse<>("SUCCESS", discountInterface.getAllDiscounts(request)));
  }
}
