package com.example.orderservice.messagequeue;

import com.example.orderservice.dto.OrderDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import org.springframework.kafka.core.KafkaTemplate;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Service
@Slf4j
public class KafkaProducer {

    private ObjectMapper mapper;
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    public KafkaProducer(
            ObjectMapper mapper,
            KafkaTemplate<String, String> kafkaTemplate
    ) {
        this.mapper = mapper;
        this.kafkaTemplate = kafkaTemplate;
    }

    public OrderDto send(String topic, OrderDto orderDto) {
        ObjectMapper mapper = new ObjectMapper();
        String payload = "";
        try {
            payload = mapper.writeValueAsString(orderDto);
        } catch(JsonProcessingException e) {
            log.error("JSON Serialize Fail", e);
            throw new RuntimeException("JSON Serialize Fail", e);
        }

        Future<SendResult<String, String>> future = kafkaTemplate.send(topic, payload);

        try {
            // 최대 5초 블로킹
            future.get(7, TimeUnit.SECONDS);
            log.info("Kafka Send Success, topic: {}, orderId: {}", topic, orderDto.getOrderId());
        } catch (TimeoutException te) {
            log.error("Kafka Send Timeout (Over 5 sec), topic: {}, orderId: {}", topic, orderDto.getOrderId());
            throw new RuntimeException("Kafka Timeout", te);
        } catch (Exception e) {
            log.error("Kafka Send Fail, topic: {}, orderId: {}", topic, orderDto.getOrderId());
            throw new RuntimeException("Kafka Fail", e);
        }

        log.info("Kafka Producer sent data from the Order microservice: " + orderDto);

        return orderDto;
    }
}
