package com.service.apigateway.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.service.apigateway.repositories.IdentityClient;
import com.service.apigateway.requests.ValidateRequest;
import com.service.apigateway.resources.ValidateResource;
import com.service.apigateway.response.SuccessResponse;

import reactor.core.publisher.Mono;

@Service
public class IdentityService {
  @Autowired
  private IdentityClient identityClient;

  public Mono<SuccessResponse<ValidateResource>> validateToken(String token) {
    ValidateRequest validateRequest = new ValidateRequest(token);

    return identityClient.validateToken(validateRequest);
  }

}
