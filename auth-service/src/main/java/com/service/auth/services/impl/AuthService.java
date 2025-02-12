package com.service.auth.services.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.service.auth.dtos.LoginDTO;
import com.service.auth.entities.User;
import com.service.auth.repositories.UserRepository;
import com.service.auth.requests.LoginRequest;
import com.service.auth.requests.RegisterUserRequest;
import com.service.auth.resources.LoginResource;
import com.service.auth.responses.ErrorResponse;
import com.service.auth.securities.JwtTokenProvider;
import com.service.auth.services.interfaces.AuthInterface;

@Service
public class AuthService implements AuthInterface {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private JwtTokenProvider jwtTokenProvider;

  private User authenticateUser(String identifier, String password) {
    Optional<User> user = userRepository.findByUsernameOrEmail(identifier, identifier);
    if (!user.isPresent() || !new BCryptPasswordEncoder().matches(password, user.get().getPassword())) {
      return null;
    }
    return user.get();
  }

  @Override
  public Object loginUser(LoginRequest loginRequest) {
    try {
      User authenticatedUser = authenticateUser(loginRequest.getIdentifier(), loginRequest.getPassword());

      if (authenticatedUser == null) {
        throw new BadCredentialsException("Invalid username/email or password");
      }

      LoginDTO loginDTO = new LoginDTO(
          authenticatedUser.getId(),
          authenticatedUser.getUsername(),
          authenticatedUser.getEmail(),
          authenticatedUser.getRole().toString());

      String token = jwtTokenProvider.generateToken(
          loginDTO.getId().toString(),
          loginDTO.getUsername(),
          loginDTO.getEmail(),
          loginDTO.getRole());

      return new LoginResource(token, loginDTO);

    } catch (BadCredentialsException e) {
      Map<String, String> errors = new HashMap<>();
      errors.put("message", e.getMessage());
      ErrorResponse errorResponse = new ErrorResponse("An error occurred during authentication", errors);
      return errorResponse;
    }
  }

  @Override
  public Object registerUser(RegisterUserRequest registerUserRequest) {
    try {
      String password = registerUserRequest.getPassword();
      String confirmPassword = registerUserRequest.getConfirmPassword();

      if (!password.equals(confirmPassword)) {
        throw new IllegalArgumentException("Password and Confirm Password must be the same");
      }

      User user = new User();
      user.setUsername(registerUserRequest.getUsername());
      user.setEmail(registerUserRequest.getEmail());
      user.setPassword(new BCryptPasswordEncoder().encode(registerUserRequest.getPassword()));

      return userRepository.save(user);
    } catch (IllegalArgumentException e) {
      Map<String, String> errors = new HashMap<>();
      errors.put("message", e.getMessage());
      ErrorResponse errorResponse = new ErrorResponse("Invalid Request", errors);
      return errorResponse;
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
