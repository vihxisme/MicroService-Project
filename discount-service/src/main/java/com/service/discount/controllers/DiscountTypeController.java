package com.service.discount.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.service.discount.entities.DiscountType;
import com.service.discount.requests.TypeRequest;
import com.service.discount.resources.TypeResource;
import com.service.discount.responses.SuccessResponse;
import com.service.discount.services.interfaces.DiscountTypeInterface;

@RestController
@RequestMapping("/v1/discount-type")
public class DiscountTypeController {
  @Autowired
  private DiscountTypeInterface discountTypeInterface;

  @PostMapping("/create")
  public ResponseEntity<?> createDiscountType(@RequestBody TypeRequest request) {
    DiscountType discountType = discountTypeInterface.createDiscountType(request);

    return ResponseEntity.ok(new SuccessResponse<>("SUCCESS", discountType));
  }

  @PutMapping("/update")
  public ResponseEntity<?> updateDiscountType(@RequestBody TypeRequest request) {
    DiscountType discountType = discountTypeInterface.updateDiscountType(request);

    return ResponseEntity.ok(new SuccessResponse<>("SUCCESS", discountType));
  }

  @DeleteMapping("/delete/{id}")
  public ResponseEntity<?> deleteDiscountType0(@PathVariable Long id) {
    Boolean isType = discountTypeInterface.deleteDiscountType(id);

    return ResponseEntity.ok(new SuccessResponse<>("SUCCESS", isType));
  }

  @GetMapping("/info/all")
  public ResponseEntity<?> getDiscountTypes() {
    List<TypeResource> discountTypes = discountTypeInterface.getDiscountTypes();

    return ResponseEntity.ok(new SuccessResponse<>("SUCCESS", discountTypes));
  }
}
