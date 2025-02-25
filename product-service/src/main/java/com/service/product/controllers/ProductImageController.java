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

import com.service.product.requests.ProductImageRequest;
import com.service.product.responses.SuccessResponse;
import com.service.product.services.interfaces.ProductImageInterface;

@RestController
@RequestMapping("/v1/product-image")
public class ProductImageController {

  @Autowired
  private ProductImageInterface productImageInterface;

  @PostMapping("/create")
  public ResponseEntity<?> createProductImage(@RequestBody List<ProductImageRequest> requests) {
    return ResponseEntity.ok(
        new SuccessResponse<>(
            "SUCCESS",
            productImageInterface.createProductImage(requests)));
  }

  @PatchMapping("/update")
  public ResponseEntity<?> updateProductImage(@RequestBody List<ProductImageRequest> requests) {
    return ResponseEntity.ok(
        new SuccessResponse<>(
            "SUCCESS",
            productImageInterface.updateProductImage(requests)));
  }

  @PostMapping("/delete")
  public ResponseEntity<?> deleteProductImage(@RequestBody List<Integer> ids) {
    return ResponseEntity.ok(
        new SuccessResponse<>(
            "SUCCESS",
            productImageInterface.deleteProductImage(ids)));
  }

  @GetMapping("/info/{productId}")
  public ResponseEntity<?> getProductImageByProductId(@PathVariable String productId) {
    return ResponseEntity.ok(
        new SuccessResponse<>(
            "SUCCESS",
            productImageInterface.getProductImageByProduct(UUID.fromString(productId))));
  }
}
