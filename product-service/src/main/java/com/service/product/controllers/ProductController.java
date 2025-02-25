package com.service.product.controllers;

import java.util.UUID;

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

import com.service.product.requests.PaginationRequest;
import com.service.product.requests.ProductRequest;
import com.service.product.responses.SuccessResponse;
import com.service.product.services.interfaces.ProductInterface;

@RestController
@RequestMapping("/v1/products")
public class ProductController {
  @Autowired
  private ProductInterface productInterface;

  @PostMapping("/create")
  public ResponseEntity<?> createProduct(@RequestBody ProductRequest request) {
    return ResponseEntity.ok(
        new SuccessResponse<>(
            "SUCCESS",
            productInterface.createProduct(request)));
  }

  @PatchMapping("/update")
  public ResponseEntity<?> updateProduct(@RequestBody ProductRequest request) {
    return ResponseEntity.ok(
        new SuccessResponse<>(
            "SUCCESS",
            productInterface.updateProduct(request)));
  }

  @DeleteMapping("/delete/{id}")
  public ResponseEntity<?> deleteProduct(@PathVariable String id) {
    return ResponseEntity.ok(
        new SuccessResponse<>(
            "SUCCESS",
            productInterface.deleteProduct(UUID.fromString(id))));
  }

  @GetMapping("/info/all")
  public ResponseEntity<?> getAllProduct(@ModelAttribute PaginationRequest request) {
    return ResponseEntity.ok(
        new SuccessResponse<>(
            "SUCCESS",
            productInterface.getAllProduct(request)));
  }

}
