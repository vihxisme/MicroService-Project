package com.service.auth.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.service.auth.requests.ValidateRequest;
import com.service.auth.resources.ValidateResource;
import com.service.auth.responses.ErrorResponse;
import com.service.auth.responses.SuccessResponse;
import com.service.auth.services.interfaces.AuthenticationInterface;

@RestController
@RequestMapping("/authenticated")
public class AuththenticationController {
  @Autowired
  private AuthenticationInterface authenticationInterface;

  @PostMapping("/validate")
  public ResponseEntity<?> validateToken(@RequestBody ValidateRequest validateRequest) {
    Object authentication = authenticationInterface.validateToken(validateRequest);

    if (authentication instanceof ValidateResource validateResource) {
      SuccessResponse<ValidateResource> success = new SuccessResponse<>("SUCCESS", validateResource);
      return ResponseEntity.ok(success);
    }

    if (authentication instanceof ErrorResponse errorResponse) {
      return ResponseEntity.unprocessableEntity().body(errorResponse);
    }

    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Network Error");
  }

  @PostMapping("/verify")
  public ResponseEntity<?> verifyToken() {
    ValidateResource validateResource = new ValidateResource(true);
    SuccessResponse<ValidateResource> success = new SuccessResponse<>("SUCCESS", validateResource);
    return ResponseEntity.ok(success);
  }
}
