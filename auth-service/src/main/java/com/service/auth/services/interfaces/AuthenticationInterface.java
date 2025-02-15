package com.service.auth.services.interfaces;

import com.service.auth.requests.ValidateRequest;

public interface AuthenticationInterface {
  Object validateToken(ValidateRequest validateRequest);
}
