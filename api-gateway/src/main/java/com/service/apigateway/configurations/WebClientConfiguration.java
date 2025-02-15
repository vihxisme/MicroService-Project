package com.service.apigateway.configurations;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import com.service.apigateway.repositories.IdentityClient;

@Configuration
public class WebClientConfiguration {

  @Bean
  public WebClient webClient() {
    return WebClient.builder().baseUrl("http://localhost:8001/identity").build();
  }

  @Bean
  @SuppressWarnings("unused")
  CorsWebFilter corsWebFilter() {
    CorsConfiguration corsConfiguration = new CorsConfiguration();
    corsConfiguration.setAllowedOrigins(List.of("*")); // Cho phép tất cả các origin
    corsConfiguration.setAllowedHeaders(List.of("*")); // Cho phép tất cả headers
    corsConfiguration.setAllowedMethods(List.of("*")); // Cho phép tất cả HTTP methods (GET, POST,...)

    UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
    urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);

    return new CorsWebFilter(urlBasedCorsConfigurationSource);
  }

  @Bean
  @SuppressWarnings("unused")
  IdentityClient identityClient(WebClient webClient) {
    HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(
        WebClientAdapter.create(webClient))
        .build();

    return factory.createClient(IdentityClient.class);
  }
}
