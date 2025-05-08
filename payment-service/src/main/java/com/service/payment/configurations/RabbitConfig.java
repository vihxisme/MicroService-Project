package com.service.payment.configurations;

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

@Configuration
public class RabbitConfig {

    @Value("${rabbitmq.exchange.payment}")
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
                ""); // Chỉ cho phép deserialization các class trong package cụ thể

        // Hoặc bạn có thể ánh xạ các class cụ thể
        Map<String, Class<?>> idClassMapping = new HashMap<>();
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
    public Queue paymentQueue() {
        return QueueBuilder.durable("payment-queue")
                .deadLetterExchange("dlx-exchange")
                .deadLetterRoutingKey("payment-dlx")
                .build();
    }

    @Bean
    public Queue dlxQueue() {
        return new Queue("payment-dlx-queue", true);
    }

    @Bean
    public Binding dlxBinding() {
        return BindingBuilder.bind(dlxQueue()).to(dlxExchange()).with("payemnt-dlx");
    }

    @Bean
    public Queue createPaymentQueue() {
        return new Queue("create-payment:queue", true, false, false);
    }

    @Bean
    public Queue cancelPaymentQueue() {
        return new Queue("cancel-payment:queue", true, false, false);
    }

    @Bean
    public Binding bindingCancelPaymentQueue(Queue cancelPaymentQueue, DirectExchange myExchange) {
        return BindingBuilder.bind(cancelPaymentQueue).to(myExchange).with("cancel-payment");
    }

    @Bean
    public Binding bindingCreatePaymentQueue(Queue createPaymentQueue, DirectExchange myExchange) {
        return BindingBuilder.bind(createPaymentQueue).to(myExchange).with("create-payment");
    }

}
