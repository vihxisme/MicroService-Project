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

import com.service.product.requests.ColorRequest;
import com.service.product.responses.SuccessResponse;
import com.service.product.services.interfaces.ColorInterface;

@RestController
@RequestMapping("/v1/color")
public class ColorController {
  @Autowired
  private ColorInterface colorInterface;

  @PostMapping("/create")
  public ResponseEntity<?> createColor(@RequestBody ColorRequest request) {
    return ResponseEntity.ok(new SuccessResponse<>("SUCCESS", colorInterface.createColor(request)));
  }

  @PatchMapping("/update")
  public ResponseEntity<?> updateColor(@RequestBody ColorRequest request) {
    return ResponseEntity.ok(new SuccessResponse<>("SUCCESS", colorInterface.updateColor(request)));
  }

  @DeleteMapping("/delete/{id}")
  public ResponseEntity<?> deleteColor(@PathVariable Integer id) {
    return ResponseEntity.ok(new SuccessResponse<>("SUCCESS", colorInterface.deleteRequest(id)));
  }

  @GetMapping("/info/all")
  public ResponseEntity<?> getAllColor() {
    return ResponseEntity.ok(new SuccessResponse<>("SUCCESS", colorInterface.getAllColor()));
  }
}
