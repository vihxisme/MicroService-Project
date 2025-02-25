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

import com.service.product.requests.CategorieRequest;
import com.service.product.requests.PaginationRequest;
import com.service.product.responses.SuccessResponse;
import com.service.product.services.interfaces.CategorieInterface;

@RestController
@RequestMapping("/v1/categorie")
public class CategorieController {
  @Autowired
  private CategorieInterface categorieInterface;

  @PostMapping("/create")
  public ResponseEntity<?> createCategorie(@RequestBody CategorieRequest request) {
    return ResponseEntity.ok(new SuccessResponse<>("SUCCESS", categorieInterface.createCategorie(request)));
  }

  @PatchMapping("/update")
  public ResponseEntity<?> updateCategorie(@RequestBody CategorieRequest request) {
    return ResponseEntity.ok(new SuccessResponse<>("SUCCESS", categorieInterface.updateCategorie(request)));
  }

  @DeleteMapping("/delete/{id}")
  public ResponseEntity<?> deleteCategorie(@PathVariable String id) {
    return ResponseEntity.ok(new SuccessResponse<>("SUCCESS", categorieInterface.deleteCategorie(UUID.fromString(id))));
  }

  @GetMapping("/info/all")
  public ResponseEntity<?> getAllCategorie() {
    return ResponseEntity.ok(new SuccessResponse<>("SUCCESS", categorieInterface.getAllCategorie()));
  }

  @GetMapping("/info/all/list")
  public ResponseEntity<?> getAllCategorie(@ModelAttribute PaginationRequest request) {
    return ResponseEntity.ok(new SuccessResponse<>("SUCCESS", categorieInterface.getAllCategorie(request)));
  }
}
