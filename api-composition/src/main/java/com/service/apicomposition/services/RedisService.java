package com.service.apicomposition.services;

import java.time.Duration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class RedisService {

    @Autowired
    private ReactiveRedisTemplate<String, Object> reactiveRedisTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    private Logger logger = LoggerFactory.getLogger(RedisService.class);

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

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "clear-cache", durable = "true"),
            exchange = @Exchange(name = "cache-update-exchange", type = "direct"),
            key = "clear-cache"
    ))
    public Mono<Void> clearCacheWithPrefix(String prefix) {
        try {
            Flux<String> keys = reactiveRedisTemplate.keys(prefix + "*");

            return keys.collectList()
                    .flatMap(keysList -> {
                        if (!keysList.isEmpty()) {
                            return reactiveRedisTemplate.delete(Flux.fromIterable(keysList)).then();
                        }
                        return Mono.empty();
                    })
                    .doOnSuccess(unused -> {
                        logger.info("Cache cleared successfully");
                    })
                    .doOnError(e -> {
                        throw new RuntimeException("Cannot clear cache: " + e.getMessage());
                    });

        } catch (Exception e) {
            return Mono.error(new RuntimeException("Cannot clear cache: " + e.getMessage()));
        }
    }
}
