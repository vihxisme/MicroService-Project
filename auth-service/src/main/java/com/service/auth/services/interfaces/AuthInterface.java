package com.service.auth.services.interfaces;

import com.service.auth.requests.LoginRequest;
import com.service.auth.requests.RegisterUserRequest;

public interface AuthInterface {
  Object loginUser(LoginRequest loginRequest);

  Object registerUser(RegisterUserRequest registerUserRequest);
}
