package com.service.apicomposition.services;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import reactor.core.publisher.Mono;

@Service
public class RedisService {

    @Autowired
    private ReactiveRedisTemplate<String, Object> reactiveRedisTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    // Lưu data vào Redis với key trong khoảng ttl
    public <T> Mono<Boolean> saveData(String key, T data, Long ttl) {
        return reactiveRedisTemplate.opsForValue().set(key, data, Duration.ofSeconds(ttl));
    }

    // Lấy dữ liệu từ Redis
    public <T> Mono<T> getData(String key, ParameterizedTypeReference<T> typeRef) {
        return reactiveRedisTemplate.opsForValue()
                .get(key)
                .map(json -> {
                    JavaType javaType = objectMapper.getTypeFactory().constructType(typeRef.getType());
                    return objectMapper.convertValue(json, objectMapper.constructType(javaType));
                });
    }
}
