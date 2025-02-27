package com.service.apicomposition.configurations;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Configuration
public class RedisConfig {

    @Value("${spring.redis.host}")
    private String redisHost;

    @Value("${spring.redis.port}")
    private int redisPort;

    @Value("${spring.redis.password}")
    private String redisPassword;

    @Bean
    @Primary
    public ReactiveRedisConnectionFactory reactiveRedisConnectionFactory() {
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
        configuration.setHostName(redisHost);
        configuration.setPort(redisPort);
        configuration.setPassword(RedisPassword.of(redisPassword));
        return new LettuceConnectionFactory(configuration);
    }

    @Bean
    public ReactiveRedisTemplate<String, Object> reactiveRedisTemplate(ReactiveRedisConnectionFactory factory) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule()); // Hỗ trợ kiểu LocalDateTime
        objectMapper.activateDefaultTyping(objectMapper.getPolymorphicTypeValidator(),
                ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);

        Jackson2JsonRedisSerializer<Object> serializer = new Jackson2JsonRedisSerializer<>(objectMapper, Object.class);

        RedisSerializationContext<String, Object> context
                = RedisSerializationContext.<String, Object>newSerializationContext(RedisSerializer.string())
                        .value(serializer)
                        .build();

        return new ReactiveRedisTemplate<>(factory, context);
    }

}
