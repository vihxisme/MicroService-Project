package com.service.auth.services.interfaces;

import com.service.auth.requests.LoginRequest;
import com.service.auth.requests.RegisterUserRequest;
import com.service.auth.wrappers.RegisterUserRequestWrapper;

public interface AuthInterface {
  Object loginUser(LoginRequest loginRequest);

  Object registerUser(RegisterUserRequest registerUserRequest);

  Object registerUser(RegisterUserRequestWrapper request);
}
