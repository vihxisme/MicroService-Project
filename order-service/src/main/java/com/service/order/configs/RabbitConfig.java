package com.service.order.configs;

import java.util.HashMap;
import java.util.Map;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.DefaultClassMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.service.order.requests.OrderItemRequest;
import com.service.order.requests.OrderRequest;
import com.service.order.requests.ShippingAddressRequest;

@Configuration
public class RabbitConfig {

    @Value("${service.rabbitmq.exchange}")
    private String exchange;

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter();
        converter.setClassMapper(classMapper()); // Giới hạn class được phép deserialize
        return converter;
    }

    @Bean
    public DefaultClassMapper classMapper() {
        DefaultClassMapper classMapper = new DefaultClassMapper();
        classMapper.setTrustedPackages("com.service.product.requests"); // Chỉ cho phép deserialization các class trong package cụ thể

        // Hoặc bạn có thể ánh xạ các class cụ thể
        Map<String, Class<?>> idClassMapping = new HashMap<>();
        idClassMapping.put("com.service.product.requests.OrderRequest", OrderRequest.class);
        idClassMapping.put("com.service.order.requests.OrderItemRequest", OrderItemRequest.class);
        idClassMapping.put("com.service.order.requests.ShippingAddressRequest", ShippingAddressRequest.class);
        classMapper.setIdClassMapping(idClassMapping);

        return classMapper;
    }

    @Bean
    public DirectExchange myExchange() {
        return new DirectExchange(exchange);
    }

    @Bean
    public Queue cacheQueue() {
        return new Queue("clear-cache:queue", true, false, true);
    }

    @Bean
    public Queue createOrderQueue() {
        return new Queue("create-order:queue", true, false, true);
    }

    @Bean
    public Queue createOrderItemQueue() {
        return new Queue("create-order-item:queue", true, false, true);
    }

    @Bean
    public Queue createShippingQueue() {
        return new Queue("create-shipping:queue", true, false, true);
    }

    @Bean
    public Binding bindingCacheQueue(Queue cacheQueue, DirectExchange exchange) {
        return BindingBuilder.bind(cacheQueue).to(exchange).with("clear-cache");
    }

    @Bean
    public Binding bindingCreateOrderQueue(Queue createOrderQueue, DirectExchange exchange) {
        return BindingBuilder.bind(createOrderQueue).to(exchange).with("create-order");
    }

    @Bean
    public Binding bindingCreateOrderItemQueue(Queue createOrderItemQueue, DirectExchange exchange) {
        return BindingBuilder.bind(createOrderItemQueue).to(exchange).with("create-order-item");
    }

    @Bean
    public Binding bindingCreateShippingQueue(Queue createShippingQueue, DirectExchange exchange) {
        return BindingBuilder.bind(createShippingQueue).to(exchange).with("create-shipping");
    }
}
