package com.service.apicomposition.helper;

import java.util.Collections;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

@Component
@AllArgsConstructor
public class WebClientExceptionHandler {
  private final ObjectMapper objectMapper;

  @SuppressWarnings("unused")
  public Mono<ResponseEntity<Object>> handleWebClientException(WebClientResponseException e) {
    Object errorBody;
    try {
      errorBody = objectMapper.readValue(e.getResponseBodyAsString(), Map.class);
    } catch (JsonProcessingException ex) {
      errorBody = Collections.singletonMap("error", e.getResponseBodyAsString());
    }
    return Mono.just(ResponseEntity.status(e.getStatusCode()).body(errorBody));
  }
}
