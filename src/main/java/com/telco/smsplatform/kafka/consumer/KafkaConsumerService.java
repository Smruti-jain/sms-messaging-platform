package com.telco.smsplatform.kafka.consumer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.telco.smsplatform.db.service.InternalDbService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class KafkaConsumerService {

    private final InternalDbService internalDbService;
    private final ObjectMapper objectMapper;

    public KafkaConsumerService(InternalDbService internalDbService, ObjectMapper objectMapper) {
        this.internalDbService = internalDbService;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "sms-topic", groupId = "sms-group")
    public void consumeMessage(String message) {
        try {
            log.info("Received message from Kafka: {}", message);
            JsonNode jsonNode = objectMapper.readTree(message);

            Long mobile = jsonNode.get("mobile").asLong();
            String msgText = jsonNode.get("message").asText();
            Integer accountId = jsonNode.get("account_id").asInt();

            internalDbService.insertSendMsg(mobile, msgText, accountId);

            log.info("Message inserted into DB for mobile: {}", mobile);

        } catch (Exception e) {
            log.error("Error consuming message: {}", e.getMessage());
        }
    }
}
