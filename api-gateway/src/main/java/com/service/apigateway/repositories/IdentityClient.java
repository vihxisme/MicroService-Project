package com.service.apigateway.repositories;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.PostExchange;

import com.service.apigateway.requests.ValidateRequest;
import com.service.apigateway.resources.ValidateResource;
import com.service.apigateway.response.SuccessResponse;

import reactor.core.publisher.Mono;

public interface IdentityClient {
  @PostExchange(url = "/authenticated/validate", contentType = MediaType.APPLICATION_JSON_VALUE)
  Mono<SuccessResponse<ValidateResource>> validateToken(@RequestBody ValidateRequest validateRequest);
}
