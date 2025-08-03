package com.telco.smsplatform.customer.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.telco.smsplatform.db.service.InternalDbService;
import com.telco.smsplatform.kafka.producer.KafkaProducerService;
import com.telco.smsplatform.customer.model.CustomerRequest;
import com.telco.smsplatform.customer.model.CustomerResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final KafkaProducerService kafkaProducerService;
    private final InternalDbService internalDbService;
    private final ObjectMapper objectMapper;

    public CustomerServiceImpl(KafkaProducerService kafkaProducerService, InternalDbService internalDbService, ObjectMapper objectMapper) {
        this.kafkaProducerService = kafkaProducerService;
        this.internalDbService = internalDbService;
        this.objectMapper = objectMapper;
    }

    @Override
    public CustomerResponse processMessage(CustomerRequest request) throws JsonProcessingException {
        // Validate username & password
        Integer accountId = internalDbService.validateUser(request.getUsername(), request.getPassword());
        if (accountId == null) {
            return new CustomerResponse("REJECTED", "FAILURE", null);
        }

        // Generate ACK ID
        String ackId = UUID.randomUUID().toString();

        // Build Kafka payload
        SmsPayload payload = new SmsPayload(
                ackId,
                accountId,
                Long.parseLong(request.getMobile()),
                request.getMessage()
        );

        // Convert to JSON and send to Kafka
        String payloadJson = objectMapper.writeValueAsString(payload);
        kafkaProducerService.sendMessage(payloadJson);

        // Return success
        return new CustomerResponse("ACCEPTED", "SUCCESS", ackId);
    }

    // Inner class for Kafka payload
    @Getter
    @AllArgsConstructor
    static class SmsPayload {
        private String ack_id;
        private int account_id;
        private long mobile;
        private String message;
    }
}
