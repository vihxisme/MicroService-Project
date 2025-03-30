package com.service.apicomposition.services;

import java.time.Duration;
import java.util.List;
import java.util.Map;

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

import com.fasterxml.jackson.core.JsonProcessingException;
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

    // public <T> Mono<Boolean> saveList(String key, List<T> data, long ttlInSeconds) {
    //     return reactiveRedisTemplate.delete(key)
    //             .thenMany(Flux.fromIterable(data))
    //             .flatMap(item -> {
    //                 Map<String, Object> map = objectMapper.convertValue(item, new TypeReference<Map<String, Object>>() {});
    //                 return reactiveRedisTemplate.opsForList().rightPush(key, map);
    //             })
    //             .then(reactiveRedisTemplate.expire(key, Duration.ofSeconds(ttlInSeconds)))
    //             .thenReturn(true)
    //             .doOnSuccess(v -> logger.info("Saved list to Redis key: {}", key))
    //             .doOnError(e -> logger.error("Error saving list to Redis: {}", e.getMessage()));
    // }
    public <T> Mono<Boolean> saveList(String key, List<T> data, long ttlInSeconds) {
        return reactiveRedisTemplate.delete(key)
                .thenMany(Flux.fromIterable(data))
                .flatMap(item -> reactiveRedisTemplate.opsForList().rightPush(key, objectMapper.convertValue(item, Map.class)))
                .then(reactiveRedisTemplate.expire(key, Duration.ofSeconds(ttlInSeconds)))
                .thenReturn(true)
                .doOnSuccess(v -> logger.info("Saved list to Redis key: {}", key))
                .doOnError(e -> logger.error("Error saving list to Redis: {}", e.getMessage()));
    }

    // public <T> Mono<List<T>> getListRange(String key, long start, long end, Class<T> clazz) {
    //     return reactiveRedisTemplate.opsForList()
    //     .range(key, start, end)
    //     .map(item -> objectMapper.convertValue(item, clazz))
    //             .collectList()
    //             .filter(list -> !list.isEmpty());
    // }
    public <T> Mono<List<T>> getListRange(String key, long start, long end, Class<T> clazz) {
        return reactiveRedisTemplate.opsForList()
                .range(key, start, end)
                .map(item -> objectMapper.convertValue(item, clazz))
                .collectList()
                .filter(list -> !list.isEmpty());
    }

    public Mono<Long> getListSize(String key) {
        return reactiveRedisTemplate.opsForList().size(key);
    }

    private <T> String serialize(T data) {
        try {
            return objectMapper.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Serialization error", e);
        }
    }

    private <T> T deserialize(Object data, Class<T> clazz) {
        return objectMapper.convertValue(data, clazz);
    }
}
