package com.service.product.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.service.product.requests.ProductDetailRequest;
import com.service.product.responses.SuccessResponse;
import com.service.product.services.interfaces.ProductDetailInterface;

@RestController
@RequestMapping("/v1/product-detail")
public class ProductDetailController {

  @Autowired
  private ProductDetailInterface productDetailInterface;

  @PostMapping("/create/tt")
  public ResponseEntity<?> create(@RequestBody ProductDetailRequest request) {
    return ResponseEntity.ok(
        new SuccessResponse<>(
            "SUCCESS",
            productDetailInterface.create(request)));
  }

  @PostMapping("/create")
  public ResponseEntity<?> createProductDetail(@RequestBody List<ProductDetailRequest> requests) {
    return ResponseEntity.ok(
        new SuccessResponse<>(
            "SUCCESS",
            productDetailInterface.createProductDetailList(requests)));
  }

  @PatchMapping("/update")
  public ResponseEntity<?> updateProductDetail(@RequestBody List<ProductDetailRequest> requests) {
    return ResponseEntity.ok(
        new SuccessResponse<>(
            "SUCCESS",
            productDetailInterface.createProductDetailList(requests)));
  }

  @PostMapping("/delete")
  public ResponseEntity<?> deleteProductDetail(@RequestBody List<Integer> ids) {
    return ResponseEntity.ok(
        new SuccessResponse<>(
            "SUCCESS",
            productDetailInterface.deleteProductDetailList(ids)));
  }

  @GetMapping("/info/{productId}")
  public ResponseEntity<?> getProductDetailById(@PathVariable String productId) {
    return ResponseEntity.ok(
        new SuccessResponse<>(
            "SUCCESS",
            productDetailInterface.getProductDetailById(UUID.fromString(productId))));
  }

}
