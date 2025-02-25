package com.service.apicomposition.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.server.ServerWebExchange;

import com.service.apicomposition.helper.WebClientExceptionHandler;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/customer-client")
@RequiredArgsConstructor
public class API_CustomerClientController {

  private Logger logger = LoggerFactory.getLogger(API_CustomerClientController.class);

  @Autowired
  private final WebClient customerClient;

  @Autowired
  private WebClientExceptionHandler webClientExceptionHandler;

  @RequestMapping("/proxy/**")
  public Mono<ResponseEntity<Object>> proxyRequest(ServerWebExchange exchange) {
    ServerHttpRequest request = exchange.getRequest();

    String path = request.getURI().getPath().replaceFirst("/customer-client/proxy", "");
    HttpMethod method = request.getMethod();

    WebClient.RequestHeadersSpec<?> requestSpec = customerClient.method(method).uri(path);

    return exchange.getRequest().getBody()
        .collectList()
        .flatMap(body -> {
          if (body.isEmpty()) {
            return ((WebClient.RequestBodySpec) requestSpec)
                .contentType(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(Object.class);
          }
          return ((WebClient.RequestBodySpec) requestSpec)
              .contentType(MediaType.APPLICATION_JSON)
              .body(BodyInserters.fromDataBuffers(Flux.fromIterable(body)))
              .retrieve()
              .bodyToMono(Object.class);
        })
        .map(ResponseEntity::ok)
        .onErrorResume(WebClientResponseException.class, webClientExceptionHandler::handleWebClientException);
  }

}
