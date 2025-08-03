package com.telco.smsplatform.kafka.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.telco.smsplatform.db.service.InternalDbService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class KafkaConsumerServiceTest {

    @Test
    void testConsumeMessage() throws Exception {
        InternalDbService dbService = Mockito.mock(InternalDbService.class);
        ObjectMapper mapper = new ObjectMapper();
        KafkaConsumerService consumer = new KafkaConsumerService(dbService, mapper);

        String jsonMessage = "{\"account_id\":1001, \"mobile\":9876543210, \"message\":\"Hello\"}";

        consumer.consumeMessage(jsonMessage);
        Mockito.verify(dbService, Mockito.times(1))
                .insertSendMsg(9876543210L, "Hello", 1001);
    }
}

