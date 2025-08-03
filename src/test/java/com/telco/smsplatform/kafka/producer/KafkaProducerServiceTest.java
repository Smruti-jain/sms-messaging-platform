package com.telco.smsplatform.kafka.producer;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.kafka.core.KafkaTemplate;

public class KafkaProducerServiceTest {

    @Test
    void testSendMessage() {
        KafkaTemplate<String, String> kafkaTemplate = Mockito.mock(KafkaTemplate.class);
        KafkaProducerService producer = new KafkaProducerService(kafkaTemplate);

        producer.sendMessage("Test Message");
        Mockito.verify(kafkaTemplate, Mockito.times(1))
                .send("sms-topic", "Test Message");
    }
}

