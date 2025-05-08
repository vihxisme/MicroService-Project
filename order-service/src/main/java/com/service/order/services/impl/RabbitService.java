package com.service.order.services.impl;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class RabbitService<T> {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    Logger logger = LoggerFactory.getLogger(RabbitService.class);

    // Gửi message không cần phản hồi
    public void sendMessage(String exchange, String routingKey, Object data) {
        try {
            MessageProperties messageProperties = new MessageProperties();
            messageProperties.setDeliveryMode(MessageDeliveryMode.PERSISTENT);

            byte[] jsonData = objectMapper.writeValueAsBytes(data);
            Message message = new Message(jsonData, messageProperties);

            rabbitTemplate.convertAndSend(exchange, routingKey, message);
            logger.info("Message sent to exchange '{}' with routingKey '{}'", exchange, routingKey);
        } catch (Exception e) {
            logger.error("Error while sending message to RabbitMQ", e);
        }
    }

    public <T> void sendMessageT(String exchange, String routingKey, T data) {
        try {
            MessageProperties messageProperties = new MessageProperties();
            messageProperties.setDeliveryMode(MessageDeliveryMode.PERSISTENT);

            byte[] jsonData = objectMapper.writeValueAsBytes(data);
            Message message = new Message(jsonData, messageProperties);

            rabbitTemplate.convertAndSend(exchange, routingKey, message);
            logger.info("Message sent to exchange '{}' with routingKey '{}'", exchange, routingKey);
        } catch (Exception e) {
            logger.error("Error while sending message to RabbitMQ", e);
        }
    }

    public <T> void sendMessageList(String exchange, String routingKey, List<T> data) {
        try {
            MessageProperties messageProperties = new MessageProperties();
            messageProperties.setDeliveryMode(MessageDeliveryMode.PERSISTENT);

            byte[] jsonData = objectMapper.writeValueAsBytes(data);
            Message message = new Message(jsonData, messageProperties);

            rabbitTemplate.convertAndSend(exchange, routingKey, message);
            logger.info("Message sent to exchange '{}' with routingKey '{}'", exchange, routingKey);
        } catch (Exception e) {
            logger.error("Error while sending message to RabbitMQ", e);
        }
    }

    // Gửi message và nhận phản hồi dạng Object
    public <T> Optional<T> sendAndReceive(String exchange, String routingKey, Object data, Class<T> responseType) {
        try {
            MessageProperties messageProperties = new MessageProperties();
            messageProperties.setDeliveryMode(MessageDeliveryMode.PERSISTENT);

            byte[] jsonData = objectMapper.writeValueAsBytes(data);
            Message message = new Message(jsonData, messageProperties);

            Object response = rabbitTemplate.convertSendAndReceive(exchange, routingKey, message);
            if (response == null) {
                logger.warn("No response received for message sent to exchange '{}' with routingKey '{}'", exchange, routingKey);
                return Optional.empty();
            }
            T result = objectMapper.convertValue(response, responseType);
            logger.info("Response received for exchange '{}' with routingKey '{}'", exchange, routingKey);
            return Optional.of(result);
        } catch (Exception e) {
            logger.error("Error while sending and receiving message to/from RabbitMQ", e);
            return Optional.empty();
        }
    }

    // Gửi message và nhận phản hồi dạng List
    public <T> Optional<List<T>> sendAndReceiveList(String exchange, String routingKey, Object data, TypeReference<List<T>> responseType) {
        try {
            MessageProperties messageProperties = new MessageProperties();
            messageProperties.setDeliveryMode(MessageDeliveryMode.PERSISTENT);

            byte[] jsonData = objectMapper.writeValueAsBytes(data);
            Message message = new Message(jsonData, messageProperties);

            Object response = rabbitTemplate.convertSendAndReceive(exchange, routingKey, message);
            if (response == null) {
                logger.warn("No response received for message sent to exchange '{}' with routingKey '{}'", exchange, routingKey);
                return Optional.empty();
            }
            List<T> result = objectMapper.convertValue(response, responseType);
            logger.info("Response received (List) for exchange '{}' with routingKey '{}'", exchange, routingKey);
            return Optional.of(result);
        } catch (Exception e) {
            logger.error("Error while sending and receiving message list to/from RabbitMQ", e);
            return Optional.empty();
        }
    }

    // Gửi Map không nhận phản hồi
    public <K, V> void sendMap(String exchange, String routingKey, Map<K, V> map) {
        try {
            byte[] jsonData = objectMapper.writeValueAsBytes(map);
            MessageProperties props = new MessageProperties();
            props.setDeliveryMode(MessageDeliveryMode.PERSISTENT);

            Message message = new Message(jsonData, props);
            rabbitTemplate.convertAndSend(exchange, routingKey, message);

            logger.info("Map sent to exchange '{}' with routingKey '{}'", exchange, routingKey);
        } catch (JsonProcessingException | AmqpException e) {
            logger.error("Error sending map to RabbitMQ", e);
        }
    }

    // Gửi Map<K, V> và nhận phản hồi là một kiểu cụ thể (Class<T>)
    public <K, V, T> Optional<T> sendMapAndReceive(String exchange, String routingKey, Map<K, V> map, Class<T> responseType) {
        try {
            byte[] jsonData = objectMapper.writeValueAsBytes(map);
            MessageProperties props = new MessageProperties();
            props.setDeliveryMode(MessageDeliveryMode.PERSISTENT);

            Message message = new Message(jsonData, props);
            Object response = rabbitTemplate.convertSendAndReceive(exchange, routingKey, message);

            if (response == null) {
                logger.warn("No response received for exchange '{}' with routingKey '{}'", exchange, routingKey);
                return Optional.empty();
            }

            T result = objectMapper.convertValue(response, responseType);
            logger.info("Response received from exchange '{}' with routingKey '{}'", exchange, routingKey);
            return Optional.of(result);
        } catch (Exception e) {
            logger.error("Error sending map and receiving response", e);
            return Optional.empty();
        }
    }

    // Gửi Map<K, V> và nhận lại Map<K, V>
    public <K, V> Optional<Map<K, V>> sendMapAndReceiveMap(String exchange, String routingKey, Map<K, V> map) {
        try {
            byte[] jsonData = objectMapper.writeValueAsBytes(map);
            MessageProperties props = new MessageProperties();
            props.setDeliveryMode(MessageDeliveryMode.PERSISTENT);

            Message message = new Message(jsonData, props);
            Object response = rabbitTemplate.convertSendAndReceive(exchange, routingKey, message);

            if (response == null) {
                logger.warn("No response received for exchange '{}' with routingKey '{}'", exchange, routingKey);
                return Optional.empty();
            }

            Map<K, V> result = objectMapper.convertValue(response, new TypeReference<Map<K, V>>() {
            });
            logger.info("Map response received from exchange '{}' with routingKey '{}'", exchange, routingKey);
            return Optional.of(result);
        } catch (Exception e) {
            logger.error("Error sending map and receiving map response", e);
            return Optional.empty();
        }
    }

    // Deserialize byte[] thành Map<K, V> (mặc định)
    public <K, V> Optional<Map<K, V>> deserializeToMap(byte[] jsonData) {
        try {
            Map<K, V> map = objectMapper.readValue(jsonData, new TypeReference<Map<K, V>>() {
            });
            return Optional.of(map);
        } catch (Exception e) {
            logger.error("Error deserializing byte[] to Map", e);
            return Optional.empty();
        }
    }

    // Deserialize byte[] thành Map<K, V> với TypeReference cụ thể
    public <K, V> Optional<Map<K, V>> deserializeToMap(byte[] jsonData, TypeReference<Map<K, V>> typeRef) {
        try {
            Map<K, V> map = objectMapper.readValue(jsonData, typeRef);
            return Optional.of(map);
        } catch (Exception e) {
            logger.error("Error deserializing byte[] to Map with TypeReference", e);
            return Optional.empty();
        }
    }

    // Deserialize JSON thành một Object cụ thể
    public <T> Optional<T> deserializeToObject(byte[] jsonData, Class<T> clazz) {
        try {
            T object = objectMapper.readValue(jsonData, clazz);
            return Optional.of(object);
        } catch (Exception e) {
            logger.error("Error while deserializing to Object", e);
            return Optional.empty();
        }
    }

    public <T> Optional<T> deserializeToGeneric(byte[] jsonData, TypeReference<T> typeReference) {
        try {
            T object = objectMapper.readValue(jsonData, typeReference);
            return Optional.of(object);
        } catch (Exception e) {
            logger.error("Error while deserializing to Object", e);
            return Optional.empty();
        }
    }

    public <T> Optional<List<T>> deserializeToListT(byte[] jsonData, TypeReference<List<T>> typeReference) {
        try {
            List<T> list = objectMapper.readValue(jsonData, typeReference);
            return Optional.of(list);
        } catch (Exception e) {
            logger.error("Error while deserializing to Object", e);
            return Optional.empty();
        }
    }
}
