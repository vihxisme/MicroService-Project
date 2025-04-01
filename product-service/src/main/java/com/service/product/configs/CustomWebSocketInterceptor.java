package com.service.product.configs;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

public class CustomWebSocketInterceptor implements HandshakeInterceptor {

    private static final List<String> PUBLIC_ORIGINS = Arrays.asList(
            "http://localhost:8888"
    );

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
            WebSocketHandler wsHandler, Map<String, Object> attributes) {
        if (request instanceof ServletServerHttpRequest) {
            ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
            String origin = servletRequest.getServletRequest().getHeader("Origin");

            if (origin != null && PUBLIC_ORIGINS.contains(origin)) {
                return true; // Cho phép kết nối
            }
        }
        return false; // Chặn kết nối nếu không nằm trong danh sách
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
            WebSocketHandler wsHandler, Exception exception) {
        // Không cần xử lý sau khi bắt tay
    }
}
