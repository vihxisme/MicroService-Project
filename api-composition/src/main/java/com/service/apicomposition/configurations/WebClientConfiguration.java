package com.service.apicomposition.configurations;

import org.springframework.beans.factory.annotation.Qualifier;
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
    @Qualifier("customerWebClient")
    public WebClient customerWebClient(WebClient.Builder webClientBuilder) {
        return webClientBuilder.baseUrl("http://customer-service/customer").build();
    }

    @Bean
    @Qualifier("productWebClient")
    public WebClient productWebClient(WebClient.Builder webClientBuilder) {
        return webClientBuilder.baseUrl("http://product-service/product").build();
    }

    @Bean
    @Qualifier("discountWebClient")
    public WebClient discountWebClient(WebClient.Builder webClientBuilder) {
        return webClientBuilder.baseUrl("http://discount-service/discount").build();
    }

    @Bean
    @Qualifier("inventoryWebClient")
    public WebClient inventoryWebClient(WebClient.Builder webClientBuilder) {
        return webClientBuilder.baseUrl("http://inventory-service/inventory").build();
    }

    @Bean
    @Qualifier("cartWebClient")
    public WebClient cartWebClient(WebClient.Builder webClientBuilder) {
        return webClientBuilder.baseUrl("http://cart-service/cart").build();
    }
}
