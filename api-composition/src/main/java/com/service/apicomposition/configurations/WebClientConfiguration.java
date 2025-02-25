package com.service.apicomposition.configurations;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfiguration {
  @Bean
  @LoadBalanced
  public WebClient.Builder webClientBuilder() {
    return WebClient.builder();
  }

  @Bean
  public WebClient customerWebClient(WebClient.Builder webClientBuilder) {
    return webClientBuilder.baseUrl("http://customer-service/customer").build();
  }

  @Bean
  public WebClient productWebClient(WebClient.Builder webClientBuilder) {
    return webClientBuilder.baseUrl("http://product-service/product").build();
  }

  @Bean
  public WebClient discountWebClient(WebClient.Builder webClientBuilder) {
    return webClientBuilder.baseUrl("http://discount-service/discount").build();
  }
}
