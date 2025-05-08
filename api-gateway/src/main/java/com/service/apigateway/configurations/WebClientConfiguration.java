package com.service.apigateway.configurations;

import java.time.Duration;
import java.util.List;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import com.service.apigateway.repositories.IdentityClient;

import io.netty.channel.ChannelOption;
import reactor.netty.http.client.HttpClient;

@Configuration
public class WebClientConfiguration {

    @Bean
    @LoadBalanced
    @ConditionalOnMissingBean(WebClient.Builder.class)
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }

    // @Bean
    // public WebClient webClient() {
    // return WebClient.builder()
    // .baseUrl("http://localhost:8001/identity")
    // .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
    // .clientConnector(new ReactorClientHttpConnector(
    // HttpClient.create()
    // .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000) // Thời gian chờ kết nối
    // là 5000ms
    // .responseTimeout(Duration.ofMillis(5000)) // Thời gian chờ phản hồi là 5000ms
    // ))
    // .build();
    // }
    @Bean
    public WebClient webClient(WebClient.Builder webClientBuilder) {
        return webClientBuilder
                .baseUrl("http://auth-service/identity")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .clientConnector(new ReactorClientHttpConnector(
                        HttpClient.create()
                                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000) // Thời gian chờ kết nối là 5000ms
                                .responseTimeout(Duration.ofMillis(5000)) // Thời gian chờ phản hồi là 5000ms
                ))
                .build();
    }

    @Bean
    CorsWebFilter corsWebFilter() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedOrigins(List.of("http://localhost:3333", "http://localhost:3000", "http://localhost:4444")); // Cho phép tất cả các origin
        corsConfiguration.setAllowedHeaders(List.of("Authorization", "Content-Type", "Accept")); // Cho phép tất cả headers
        corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")); // Cho phép tất cả HTTP methods (GET, POST,...)
        corsConfiguration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);

        return new CorsWebFilter(urlBasedCorsConfigurationSource);
    }

    @Bean
    IdentityClient identityClient(WebClient webClient) {
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(
                WebClientAdapter.create(webClient))
                .build();

        return factory.createClient(IdentityClient.class);
    }
}
