package com.service.product.controllers;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.service.product.services.interfaces.ProductInterface;

@RestController
@RequestMapping("/internal")
public class InternalController {
  @Autowired
  private ProductInterface productInterface;

  @GetMapping("/products/list")
  public ResponseEntity<?> getAllProductElseInactive() {
    return ResponseEntity.ok(productInterface.getAllProductElseInactive());
  }

  @GetMapping("/products/categorie")
  public ResponseEntity<?> getAllProductByCategorie(@RequestParam String categorieId) {
    return ResponseEntity.ok(productInterface.getAllProductByCategorie(UUID.fromString(categorieId)));
  }
}
