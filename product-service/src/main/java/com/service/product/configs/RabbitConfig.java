package com.service.product.configs;

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
import com.service.product.requests.ProductDetailRequest;
import com.service.product.requests.ProductImageRequest;
import com.service.product.requests.VariantRequest;
import com.service.product.resources.ProductVariantResource;
import com.service.product.wrapper.ProdDetailsWrapper;
import com.service.product.wrapper.ProdImageWrapper;

@Configuration
public class RabbitConfig {

    @Value("${rabbitmq.exchange.product}")
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
                "com.service.product.requests",
                "com.service.product.wrapper",
                "com.service.events.dto",
                "com.service.product.resources");

        // Hoặc bạn có thể ánh xạ các class cụ thể
        Map<String, Class<?>> idClassMapping = new HashMap<>();
        idClassMapping.put("com.service.product.requests.VariantRequest", VariantRequest.class);
        idClassMapping.put("com.service.product.requests.ProductDetailRequest", ProductDetailRequest.class);
        idClassMapping.put("com.service.product.requests.ProductImageRequest", ProductImageRequest.class);
        idClassMapping.put("com.service.product.wrapper.ProdDetailsWrapper", ProdDetailsWrapper.class);
        idClassMapping.put("com.service.product.wrapper.ProdImageWrapper", ProdImageWrapper.class);
        idClassMapping.put("com.service.events.dto.InventoryEvent", InventoryEvent.class);
        idClassMapping.put("com.service.product.resources.ProductVariantResource", ProductVariantResource.class);
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
    public Queue cacheQueue() {
        return new Queue("clear-cache:queue", true, false, true);
    }

    @Bean
    public Queue createProdVariantQueue() {
        return new Queue("create-prod-variant:queue", true, false, true);
    }

    @Bean
    public Queue createProdImageQueue() {
        return new Queue("create-prod-image:queue", true, false, true);
    }

    @Bean
    public Queue createProdDetailsQueue() {
        return new Queue("create-prod-details:queue", true, false, true);
    }

    @Bean
    public Queue updateVariantQuantityQueue() {
        return new Queue("update-variant-quantity:queue", true, false, true);
    }

    @Bean
    public Queue updateProductStatusQueue() {
        return new Queue("update-product-status:queue", true, false, true);
    }

    @Bean
    public Binding bindingCacheQueue(Queue cacheQueue, DirectExchange exchange) {
        return BindingBuilder.bind(cacheQueue).to(exchange).with("clear-cache");
    }

    @Bean
    public Binding bindingCreateProdVariantQueue(Queue createProdVariantQueue, DirectExchange exchange) {
        return BindingBuilder.bind(createProdVariantQueue).to(exchange).with("create-prod-variant");
    }

    @Bean
    public Binding bindingCreateProdImageQueue(Queue createProdImageQueue, DirectExchange exchange) {
        return BindingBuilder.bind(createProdImageQueue).to(exchange).with("create-prod-image");
    }

    @Bean
    public Binding bindingCreateProdDetailsQueue(Queue createProdDetailsQueue, DirectExchange exchange) {
        return BindingBuilder.bind(createProdDetailsQueue).to(exchange).with("create-prod-details");
    }

    @Bean
    public Binding bindingUpdateVariantQuantityQueue(Queue updateVariantQuantityQueue, DirectExchange exchange) {
        return BindingBuilder.bind(updateVariantQuantityQueue).to(exchange).with("update-variant-quantity");
    }

    @Bean
    public Binding bindingUpdateProductStatusQueue(Queue updateProductStatusQueue, DirectExchange exchange) {
        return BindingBuilder.bind(updateProductStatusQueue).to(exchange).with("update-product-status");
    }
}
