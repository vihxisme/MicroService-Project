package com.service.auth.services.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.service.auth.dtos.LoginDTO;
import com.service.auth.entities.User;
import com.service.auth.repositories.ApiCustomerClient;
import com.service.auth.repositories.UserRepository;
import com.service.auth.requests.AddInfoCustomerRequest;
import com.service.auth.requests.LoginRequest;
import com.service.auth.requests.RegisterUserRequest;
import com.service.auth.resources.LoginResource;
import com.service.auth.responses.ErrorResponse;
import com.service.auth.responses.SuccessResponse;
import com.service.auth.securities.JwtTokenProvider;
import com.service.auth.services.interfaces.AuthInterface;
import com.service.auth.wrappers.RegisterUserRequestWrapper;

import jakarta.transaction.Transactional;

@Service
public class AuthService implements AuthInterface {

  private Logger logger = LoggerFactory.getLogger(AuthService.class);

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private ApiCustomerClient apiCustomerClient;

  @Autowired
  private JwtTokenProvider jwtTokenProvider;

  @Autowired
  private ObjectMapper objectMapper;

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

      ResponseEntity<?> response = apiCustomerClient.getUserIdByAuthId(authenticatedUser.getId().toString());

      SuccessResponse<?> successResponse = objectMapper.convertValue(response.getBody(),
          new TypeReference<SuccessResponse<UUID>>() {
          });

      LoginDTO loginDTO = new LoginDTO(
          authenticatedUser.getId(),
          UUID.fromString(successResponse.getData().toString()),
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
      ErrorResponse errorResponse = new ErrorResponse(401, "An error occurred during authentication", errors);
      return errorResponse;
    }
  }

  @Override
  public Object registerUser(RegisterUserRequest registerUserRequest) {
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
  }

  @Override
  @Transactional
  public Object registerUser(RegisterUserRequestWrapper request) {
    String password = request.getRegisterUserRequest().getPassword();
    String confirmPassword = request.getRegisterUserRequest().getConfirmPassword();

    if (!password.equals(confirmPassword)) {
      throw new IllegalArgumentException("Password and Confirm Password must be the same");
    }

    User user = new User();
    user.setUsername(request.getRegisterUserRequest().getUsername());
    user.setEmail(request.getRegisterUserRequest().getEmail());
    user.setPassword(new BCryptPasswordEncoder().encode(request.getRegisterUserRequest().getPassword()));

    User addUser = userRepository.save(user);

    AddInfoCustomerRequest addInfoCustomerRequest = new AddInfoCustomerRequest(
        addUser.getId(),
        request.getInfoCustomer().getFirstName(),
        request.getInfoCustomer().getLastName(),
        addUser.getEmail(),
        request.getInfoCustomer().getPhone());

    apiCustomerClient.addInfoCustomer(addInfoCustomerRequest);

    return addUser;
  }

}
