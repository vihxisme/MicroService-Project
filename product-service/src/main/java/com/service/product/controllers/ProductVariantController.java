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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.service.product.requests.ProductVariantRequest;
import com.service.product.responses.SuccessResponse;
import com.service.product.services.interfaces.ProductVariantInterface;
import com.service.product.wrapper.ProductVariantWrapper;

@RestController
@RequestMapping("/v1/product-variant")
public class ProductVariantController {

  @Autowired
  private ProductVariantInterface productVariantInterface;

  @PostMapping("/create")
  public ResponseEntity<?> createProductVariant(@RequestBody ProductVariantWrapper requestWrapper) {
    return ResponseEntity.ok(
        new SuccessResponse<>(
            "SUCCESS",
            productVariantInterface.createProductVariant(
                requestWrapper.getProductVariantRequest(),
                requestWrapper.getVariantRequest())));
  }

  @PatchMapping("/update")
  public ResponseEntity<?> updateProductVariant(@RequestBody ProductVariantRequest request) {
    return ResponseEntity.ok(
        new SuccessResponse<>(
            "SUCCESS",
            productVariantInterface.updateProductVariant(request)));
  }

  @DeleteMapping("/delete/{id}")
  public ResponseEntity<?> deleteProductVariant(@PathVariable Integer id) {
    return ResponseEntity.ok(
        new SuccessResponse<>(
            "SUCCESS",
            productVariantInterface.deleteProductVariant(id)));
  }

  @GetMapping("/sizes/product/{productId}")
  public ResponseEntity<?> getSizesFromVariant(@PathVariable String productId) {
    return ResponseEntity.ok(
        new SuccessResponse<>(
            "SUCCESS",
            productVariantInterface.getSizesFromVariant(UUID.fromString(productId))));
  }

  @GetMapping("/colors/product/{productId}")
  public ResponseEntity<?> getcolorsFromVariant(@PathVariable String productId) {
    return ResponseEntity.ok(
        new SuccessResponse<>(
            "SUCCESS",
            productVariantInterface.getColorsFromVariant(UUID.fromString(productId))));
  }

  @GetMapping("/sizes/product")
  public ResponseEntity<?> getSizeFromVariantForProduct(@RequestParam UUID productId, @RequestParam Integer colorId) {
    return ResponseEntity.ok(
        new SuccessResponse<>(
            "SUCCESS",
            productVariantInterface.getSizeFromVariantForProduct(productId, colorId)));
  }

  @GetMapping("/colors/product")
  public ResponseEntity<?> getVariantByProductIdAndColorId(@RequestParam UUID productId,
      @RequestParam Integer colorId) {
    return ResponseEntity.ok(
        new SuccessResponse<>(
            "SUCCESS",
            productVariantInterface.getVariantByProductIdAndColorId(productId, colorId)));
  }
}
