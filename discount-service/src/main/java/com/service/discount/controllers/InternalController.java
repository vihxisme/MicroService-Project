package com.service.discount.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.service.discount.responses.SuccessResponse;
import com.service.discount.services.interfaces.DiscountInterface;

@RestController
@RequestMapping("/internal")
public class InternalController {
  @Autowired
  private DiscountInterface discountInterface;  

  @GetMapping("/discount-client/info")
  public ResponseEntity<?> getAllDiscountClient() {
    return ResponseEntity.ok(
      new SuccessResponse<>(
        "SUCCESS", 
        discountInterface.getAllDiscountsClient())
    );
  }
}
