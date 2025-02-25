package com.service.apigateway.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

  private final Logger logger = LoggerFactory.getLogger(IdentityService.class);

  public Mono<SuccessResponse<ValidateResource>> validateToken(String token) {
    ValidateRequest validateRequest = new ValidateRequest(token);

    return identityClient.validateToken(validateRequest)
        .doOnSuccess(response -> logger.info("Validation response: {}", response))
        .doOnError(error -> logger.error("Validation error: ", error));
  }

}
