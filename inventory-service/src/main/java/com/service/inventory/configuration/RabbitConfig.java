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
import com.service.events.dto.UpdateProductStatusDTO;
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
        idClassMapping.put("com.service.events.dto.UpdateProductStatusDTO", UpdateProductStatusDTO.class);
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
    public Queue inventoryItemCheckQueue() {
        return new Queue("check-inventory-item:queue", true, false, false);
    }

    @Bean
    public Queue updateInventoryItemQueue() {
        return new Queue("update-inventory-item:queue", true, false, true);
    }

    @Bean
    public Queue createStockMvmQueue() {
        return new Queue("create-stock-mvm:queue", true, false, false);
    }

    @Bean
    public Queue updateProductStatusQueue() {
        return new Queue("test-test:queue", true, false, false);
    }

    @Bean
    public Queue inventoryItemStockQueue() {
        return new Queue("inventory-item-stock:queue", true, false, false);
    }

    @Bean
    public Binding bindingInventoryQueue(Queue inventoryQueue, DirectExchange exchange) {
        return BindingBuilder.bind(inventoryQueue).to(exchange).with("create-inventory");
    }

    @Bean
    public Binding bindingInventoryItemQueue(Queue inventoryItemQueue, DirectExchange exchange) {
        return BindingBuilder.bind(inventoryItemQueue).to(exchange).with("create-inventory-item");
    }

    @Bean
    public Binding bindingUpdateInventoryItemQueue(Queue updateInventoryItemQueue, DirectExchange exchange) {
        return BindingBuilder.bind(updateInventoryItemQueue).to(exchange).with("update-inventory-item");
    }

    @Bean
    public Binding bindingInventoryItemCheckQueue(Queue inventoryItemCheckQueue, DirectExchange exchange) {
        return BindingBuilder.bind(inventoryItemCheckQueue).to(exchange).with("check-inventory-item");
    }

    @Bean
    public Binding bindingCreateStockMvmQueue(Queue createStockMvmQueue, DirectExchange exchange) {
        return BindingBuilder.bind(createStockMvmQueue).to(exchange).with("create-stock-mvm");
    }

    @Bean
    public Binding bindingUpdateProductStatusQueue(Queue updateProductStatusQueue, DirectExchange exchange) {
        return BindingBuilder.bind(updateProductStatusQueue).to(exchange).with("test-test");
    }

    @Bean
    public Binding bindingInventoryItemStockQueue(Queue inventoryItemStockQueue, DirectExchange exchange) {
        return BindingBuilder.bind(inventoryItemStockQueue).to(exchange).with("inventory-item-stock");
    }
}
