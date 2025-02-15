package com.service.auth.services.impl;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.service.auth.requests.ValidateRequest;
import com.service.auth.resources.ValidateResource;
import com.service.auth.responses.ErrorResponse;
import com.service.auth.securities.JwtTokenProvider;
import com.service.auth.services.interfaces.AuthenticationInterface;

@Service
public class AuthenticationService implements AuthenticationInterface {

  private Logger logger = LoggerFactory.getLogger(AuthenticationService.class);

  @Autowired
  private JwtTokenProvider jwtTokenProvider;

  @Override
  public Object validateToken(ValidateRequest validateRequest) {
    String token = jwtTokenProvider.resolveToken(validateRequest.getToken());
    logger.info("Token Receive: " + token);

    if (token == null || !jwtTokenProvider.isTokenFormatValid(token)
        || !jwtTokenProvider.isSignatureValid(token) || !jwtTokenProvider.isIssuerToken(token)
        || !jwtTokenProvider.isTokenExpired(token)) {
      Map<String, String> errors = new HashMap<>();
      errors.put("message", "Invalid Token!");
      ErrorResponse errorResponse = new ErrorResponse(401, "Unauthorized", errors);
      return errorResponse;
    }

    ValidateResource validateResource = new ValidateResource(true);
    return validateResource;
  }
}
