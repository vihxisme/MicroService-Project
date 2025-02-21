package com.service.apigateway.configurations;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.service.apigateway.response.ErrorResponse;
import com.service.apigateway.services.IdentityService;

import reactor.core.publisher.Mono;

@Component
public class AuthenticationFilter implements GlobalFilter, Ordered {

  private final Logger logger = LoggerFactory.getLogger(AuthenticationFilter.class);

  @Autowired
  private IdentityService identityService;

  private final String[] PUBLIC_ENDPOINT = {
      "/identity/auth/.*",
      "/identity/authenticated/.*"
  };

  @Value("${app.api-prefix}")
  private String apiPrefix;

  @Override
  public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
    ServerHttpRequest request = exchange.getRequest();

    // logger.info("Incoming request: " + request.getURI());
    // logger.info("Authorization Header: " +
    // request.getHeaders().get(HttpHeaders.AUTHORIZATION));
    // logger.info("Path: " + isPublicEndpoint(request));

    // Kiểm tra nếu request là public API (bỏ qua xác thực)
    if (isPublicEndpoint(request)) {
      return chain.filter(exchange);
    }

    // Lấy token từ Authorization header
    List<String> authHeaders = request.getHeaders().get(HttpHeaders.AUTHORIZATION);
    if (authHeaders == null || authHeaders.isEmpty()) {
      return unauthorizedResponse(exchange.getResponse());
    }

    String token = authHeaders.get(0);
    // logger.info("Bearer Token: " + token);

    // Gọi IdentityService để xác thực token
    return identityService.validateToken(token)
        .flatMap(response -> {
          if (response.getData().getValid()) {
            return chain.filter(exchange); // Token hợp lệ, tiếp tục request
          } else {
            return unauthorizedResponse(exchange.getResponse()); // Token không hợp lệ
          }
        })
        // .onErrorResume(e -> unauthorizedResponse(exchange.getResponse())); // Xử lý
        // lỗi nếu có
        .onErrorResume(e -> {
          logger.error("Error during token validation", e); // Thêm log lỗi chi tiết
          return unauthorizedResponse(exchange.getResponse()); // Xử lý lỗi nếu có
        });
  }

  private boolean isPublicEndpoint(ServerHttpRequest request) {
    String path = request.getURI().getPath();
    return Arrays.stream(PUBLIC_ENDPOINT)
        .anyMatch(endpoint -> path.matches(apiPrefix + endpoint));
  }

  private Mono<Void> unauthorizedResponse(ServerHttpResponse response) {
    response.setStatusCode(HttpStatus.UNAUTHORIZED);
    response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

    // Tạo lỗi response
    Map<String, String> errors = new HashMap<>();
    HttpStatus statusCode = (HttpStatus) response.getStatusCode();
    errors.put("message", statusCode != null ? statusCode.toString() : "Unknown error");

    ErrorResponse errorResponse = new ErrorResponse(401, "Unauthorized", errors);

    // Chuyển thành JSON
    ObjectMapper objectMapper = new ObjectMapper();
    try {
      byte[] responseBody = objectMapper.writeValueAsBytes(errorResponse);
      DataBuffer buffer = response.bufferFactory().wrap(responseBody);
      return response.writeWith(Mono.just(buffer));
    } catch (JsonProcessingException e) {
      return response.setComplete();
    }
  }

  @Override
  public int getOrder() {
    return -1;
  }

}