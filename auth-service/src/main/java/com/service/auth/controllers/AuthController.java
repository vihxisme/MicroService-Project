package com.service.auth.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.service.auth.entities.User;
import com.service.auth.requests.LoginRequest;
import com.service.auth.requests.RegisterUserRequest;
import com.service.auth.resources.LoginResource;
import com.service.auth.responses.ErrorResponse;
import com.service.auth.responses.SuccessResponse;
import com.service.auth.services.interfaces.AuthInterface;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {

  @Autowired
  private AuthInterface authInterface;

  @PostMapping("/login")
  public ResponseEntity<?> loginUser(@Valid @RequestBody LoginRequest loginRequest) {
    Object auth = authInterface.loginUser(loginRequest);

    if (auth instanceof LoginResource loginResource) {
      SuccessResponse<LoginResource> successResponse = new SuccessResponse<>("SUCCESS", loginResource);
      return ResponseEntity.ok(successResponse);
    }

    if (auth instanceof ErrorResponse errorResponse) {
      return ResponseEntity.unprocessableEntity().body(errorResponse);
    }

    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Network Error");
  }

  @PostMapping("/register")
  public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterUserRequest registerUserRequest) {
    Object auth = authInterface.registerUser(registerUserRequest);

    if (auth instanceof User user) {
      SuccessResponse<User> successResponse = new SuccessResponse<>("SUCCESS", user);
      return ResponseEntity.ok(successResponse);
    }

    if (auth instanceof ErrorResponse errorResponse) {
      return ResponseEntity.unprocessableEntity().body(errorResponse);
    }

    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Network Error");
  }

}
