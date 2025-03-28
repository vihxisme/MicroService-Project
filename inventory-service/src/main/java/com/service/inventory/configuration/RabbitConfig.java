package com.service.inventory.configuration;

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

import com.service.events.dto.InventoryEvent;
import com.service.events.dto.UpdateVariantQuantityDTO;
import com.service.inventory.wrappers.InventoryItemWrapper;

@Configuration
public class RabbitConfig {

    @Value("${rabbitmq.exchange.inventory}")
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
        classMapper.setTrustedPackages("com.service.events", "com.service.inventory.wrappers");

        // Hoặc bạn có thể ánh xạ các class cụ thể
        Map<String, Class<?>> idClassMapping = new HashMap<>();
        idClassMapping.put("com.service.events.dto.InventoryEvent", InventoryEvent.class);
        idClassMapping.put("com.service.inventory.wrappers.InventoryItemWrapper", InventoryItemWrapper.class);
        idClassMapping.put("com.service.events.dto.UpdateVariantQuantityDTO", UpdateVariantQuantityDTO.class);
        classMapper.setIdClassMapping(idClassMapping);

        return classMapper;
    }

    @Bean
    public DirectExchange myExchange() {
        return new DirectExchange(exchange);
    }

    @Bean
    public Queue inventoryQueue() {
        return new Queue("create-inventory:queue", true, false, false);
    }

    @Bean
    public Queue inventoryItemQueue() {
        return new Queue("create-inventory-item:queue", true, false, true);
    }

    @Bean
    public Binding bindingInventoryQueue(Queue inventoryQueue, DirectExchange exchange) {
        return BindingBuilder.bind(inventoryQueue).to(exchange).with("create-inventory");
    }

    @Bean
    public Binding bindingInventoryItemQueue(Queue inventoryItemQueue, DirectExchange exchange) {
        return BindingBuilder.bind(inventoryItemQueue).to(exchange).with("create-inventory-item");
    }
}
