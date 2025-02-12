package com.service.auth.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.service.auth.entities.User;
import com.service.auth.requests.AddUserRequest;
import com.service.auth.responses.ErrorResponse;
import com.service.auth.responses.SuccessResponse;
import com.service.auth.services.interfaces.UserInterface;

@RestController
@RequestMapping("/users")
public class UserController {
  @Autowired
  private UserInterface userInterface;

  @PostMapping("/add")
  public ResponseEntity<?> addUser(@RequestBody AddUserRequest addUserRequest) {
    Object userAdd = userInterface.addUser(addUserRequest);

    if (userAdd instanceof User user) {
      SuccessResponse<User> successResponse = new SuccessResponse<>("SUCCESS", user);
      return ResponseEntity.ok(successResponse);
    }

    if (userAdd instanceof ErrorResponse errorResponse) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Network Error");
  }
}
