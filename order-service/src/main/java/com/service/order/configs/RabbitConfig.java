package com.service.order.configs;

import java.util.HashMap;
import java.util.Map;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.DefaultClassMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.service.order.dtos.OrderDTO;
import com.service.order.requests.OrderItemRequest;
import com.service.order.requests.OrderRequest;
import com.service.order.requests.ShippingAddressRequest;
import com.service.order.wrappers.OrderItemReqWrapper;
import com.service.order.wrappers.CheckInventoryWrapper;

@Configuration
public class RabbitConfig {

    @Value("${rabbitmq.exchange.order}")
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
        classMapper.setTrustedPackages(
                "com.service.order.requests",
                "com.service.order.dtos",
                "com.service.order.wrappers"); // Chỉ cho phép deserialization các class trong package cụ thể

        // Hoặc bạn có thể ánh xạ các class cụ thể
        Map<String, Class<?>> idClassMapping = new HashMap<>();
        idClassMapping.put("com.service.order.requests.OrderRequest", OrderRequest.class);
        idClassMapping.put("com.service.order.requests.OrderItemRequest", OrderItemRequest.class);
        idClassMapping.put("com.service.order.requests.ShippingAddressRequest", ShippingAddressRequest.class);
        idClassMapping.put("com.service.order.dtos.OrderDTO", OrderDTO.class);
        idClassMapping.put("com.service.order.wrappers.OrderItemReqWrapper", OrderItemReqWrapper.class);
        idClassMapping.put("com.service.order.wrappers.CheckInventoryWrapper", CheckInventoryWrapper.class);
        classMapper.setIdClassMapping(idClassMapping);

        return classMapper;
    }

    @Bean
    public DirectExchange myExchange() {
        return new DirectExchange(exchange);
    }

    @Bean
    public DirectExchange dlxExchange() {
        return new DirectExchange("dlx-exchange");
    }

    @Bean
    public Queue orderQueue() {
        return QueueBuilder.durable("order-queue")
                .deadLetterExchange("dlx-exchange")
                .deadLetterRoutingKey("order-dlx")
                .build();
    }

    @Bean
    public Queue dlxQueue() {
        return new Queue("order-dlx-queue", true);
    }

    @Bean
    public Queue createOrderQueue() {
        return new Queue("create-order:queue", true, false, false);
    }

    @Bean
    public Queue createOrderItemQueue() {
        return new Queue("create-order-item:queue", true, false, false);
    }

    @Bean
    public Queue createShippingQueue() {
        return new Queue("create-shipping:queue", true, false, false);
    }

    @Bean
    public Queue testMqQueue() {
        return new Queue("test-mq:queue", true, false, false);
    }

    @Bean
    public Queue checkInventoryTaskQueue() {
        return new Queue("check-inventory-task:queue", true, false, false);
    }

    @Bean
    public Binding bindingcheckInventoryTaskQueue(Queue checkInventoryTaskQueue, DirectExchange myExchange) {
        return BindingBuilder.bind(checkInventoryTaskQueue).to(myExchange).with("check-inventory-task");
    }

    @Bean
    public Binding bindingOrderQueue(Queue testMqQueue, DirectExchange myExchange) {
        return BindingBuilder.bind(testMqQueue).to(myExchange).with("test-mq");
    }

    @Bean
    public Binding bindingCreateOrderQueue(Queue createOrderQueue, DirectExchange myExchange) {
        return BindingBuilder.bind(createOrderQueue).to(myExchange).with("create-order");
    }

    @Bean
    public Binding bindingCreateOrderItemQueue(Queue createOrderItemQueue, DirectExchange myExchange) {
        return BindingBuilder.bind(createOrderItemQueue).to(myExchange).with("create-order-item");
    }

    @Bean
    public Binding bindingCreateShippingQueue(Queue createShippingQueue, DirectExchange myExchange) {
        return BindingBuilder.bind(createShippingQueue).to(myExchange).with("create-shipping");
    }

    @Bean
    public Binding dlxBinding() {
        return BindingBuilder.bind(dlxQueue()).to(dlxExchange()).with("order-dlx");
    }

}
