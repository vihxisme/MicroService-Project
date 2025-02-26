package com.service.discount.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.service.discount.entities.DiscountTarget;
import com.service.discount.requests.PaginationRequest;
import com.service.discount.requests.TargetRequest;
import com.service.discount.responses.SuccessResponse;
import com.service.discount.services.interfaces.DiscountTargetInterface;

@RestController
@RequestMapping("/v1/discount-target")
public class DiscountTargetController {
  @Autowired
  private DiscountTargetInterface discountTargetInterface;

  @PostMapping("/create")
  public ResponseEntity<?> createDiscountTarget(@RequestBody TargetRequest request) {
    DiscountTarget discountTarget = discountTargetInterface.createDiscountTarget(request);

    return ResponseEntity.ok(new SuccessResponse<>("SUCCESS", discountTarget));
  }

  @PatchMapping("/update")
  public ResponseEntity<?> updateDiscountTarget(@RequestBody TargetRequest request) {
    DiscountTarget discountTarget = discountTargetInterface.updateDiscountTarget(request);

    return ResponseEntity.ok(new SuccessResponse<>("SUCCESS", discountTarget));
  }

  @DeleteMapping("/delete/{id}")
  public ResponseEntity<?> deleteDiscountTarget(@PathVariable Integer id) {
    Boolean isDiscountTarget = discountTargetInterface.deleteDiscountTarget(id);

    return ResponseEntity.ok(new SuccessResponse<>("SUCCESS", isDiscountTarget));
  }

  @GetMapping("/info/all")
  public ResponseEntity<?> getAllDiscountTargets(@ModelAttribute PaginationRequest request) {
    return ResponseEntity.ok(new SuccessResponse<>("SUCCESS", discountTargetInterface.getAllDiscountTargets(request)));
  }

  @GetMapping("/info/target-names")
  public ResponseEntity<?> getDiscountWithTargetName(@ModelAttribute PaginationRequest request) {
    return ResponseEntity.ok(
        new SuccessResponse<>(
            "SUCCESS",
            discountTargetInterface.getDiscountWithTargetName(request)));
  }
}
