package com.service.product.configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private static final String[] PUBLIC_ORIGINS = {
        "http://localhost:8888"
    };

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic"); // Kênh cho client subscribe
        config.setApplicationDestinationPrefixes("/app"); // Tiền tố gửi request từ client
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws/product") // Địa chỉ WebSocket
                .setAllowedOriginPatterns("*") // Chấp nhận mọi origin
                // .addInterceptors(new CustomWebSocketInterceptor())
                .withSockJS(); // Hỗ trợ SockJS (cho trình duyệt cũ)
    }
}
