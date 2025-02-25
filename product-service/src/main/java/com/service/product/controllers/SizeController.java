package com.service.product.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.service.product.requests.SizeRequest;
import com.service.product.responses.SuccessResponse;
import com.service.product.services.interfaces.SizeInterface;

@RestController
@RequestMapping("/v1/size")
public class SizeController {
  @Autowired
  private SizeInterface sizeInterface;

  @PostMapping("/create")
  public ResponseEntity<?> createSize(@RequestBody SizeRequest request) {
    return ResponseEntity.ok(new SuccessResponse<>("SUCCESS", sizeInterface.createSize(request)));
  }

  @PatchMapping("/update")
  public ResponseEntity<?> updateSize(@RequestBody SizeRequest request) {
    return ResponseEntity.ok(new SuccessResponse<>("SUCCESS", sizeInterface.updateSize(request)));
  }

  @DeleteMapping("/delete/{id}")
  public ResponseEntity<?> deleteSize(@PathVariable Integer id) {
    return ResponseEntity.ok(new SuccessResponse<>("SUCCESS", sizeInterface.deleteSize(id)));
  }

  @GetMapping("/info/all")
  public ResponseEntity<?> getAllSize() {
    return ResponseEntity.ok(new SuccessResponse<>("SUCCESS", sizeInterface.getAllSize()));
  }
}
