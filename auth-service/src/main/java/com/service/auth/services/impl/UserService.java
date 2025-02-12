package com.service.auth.services.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.service.auth.entities.User;
import com.service.auth.enums.Role;
import com.service.auth.repositories.UserRepository;
import com.service.auth.requests.AddUserRequest;
import com.service.auth.responses.ErrorResponse;
import com.service.auth.services.interfaces.UserInterface;

@Service
public class UserService implements UserInterface {

  @Autowired
  private UserRepository userRepository;

  @Override
  public Object addUser(AddUserRequest addUserRequest) throws DataIntegrityViolationException {
    try {
      Role role;
      try {
        role = Role.valueOf(addUserRequest.getRole().toUpperCase());
      } catch (IllegalArgumentException e) {
        Map<String, String> errors = new HashMap<>();
        errors.put("message", e.getMessage());
        ErrorResponse errorResponse = new ErrorResponse("Invalid Request", errors);
        return errorResponse;
      }

      User user = new User();
      user.setUsername(addUserRequest.getUsername());
      user.setEmail(addUserRequest.getEmail());
      user.setPassword(new BCryptPasswordEncoder().encode(addUserRequest.getPassword()));
      user.setRole(role);

      return userRepository.save(user);

    } catch (DataIntegrityViolationException e) {
      Map<String, String> errors = new HashMap<>();
      errors.put("message", e.getMessage());
      ErrorResponse errorResponse = new ErrorResponse("Username/Email is exist", errors);
      return errorResponse;
    } catch (Exception e) {
      Map<String, String> errors = new HashMap<>();
      errors.put("message", e.getMessage());
      ErrorResponse errorResponse = new ErrorResponse("System Error", errors);
      return errorResponse;
    }
  }
}
