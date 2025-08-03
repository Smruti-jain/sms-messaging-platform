package com.telco.smsplatform.customer.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.telco.smsplatform.customer.model.CustomerRequest;
import com.telco.smsplatform.customer.model.CustomerResponse;
import com.telco.smsplatform.db.service.InternalDbService;
import com.telco.smsplatform.kafka.producer.KafkaProducerService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class CustomerServiceTest {

    @Autowired
    private CustomerServiceImpl customerService;

    @MockBean
    private KafkaProducerService kafkaProducerService;

    @MockBean
    private InternalDbService internalDbService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testProcessMessage_ValidCredentials() throws JsonProcessingException {
        CustomerRequest request = new CustomerRequest();
        request.setUsername("user1");
        request.setPassword("password1");
        request.setMobile("9876543210");
        request.setMessage("Hello");

        Mockito.when(internalDbService.validateUser("user1", "password1")).thenReturn(1001);

        CustomerResponse response = customerService.processMessage(request);

        assertThat(response.getStatus()).isEqualTo("ACCEPTED");
        assertThat(response.getResponseCode()).isEqualTo("SUCCESS");
        Mockito.verify(kafkaProducerService, Mockito.times(1)).sendMessage(Mockito.anyString());
    }

    @Test
    void testProcessMessage_InvalidCredentials() throws JsonProcessingException {
        CustomerRequest request = new CustomerRequest();
        request.setUsername("wrong");
        request.setPassword("wrong");
        request.setMobile("9876543210");
        request.setMessage("Hello");

        Mockito.when(internalDbService.validateUser("wrong", "wrong")).thenReturn(null);

        CustomerResponse response = customerService.processMessage(request);

        assertThat(response.getStatus()).isEqualTo("REJECTED");
        assertThat(response.getResponseCode()).isEqualTo("FAILURE");
        Mockito.verify(kafkaProducerService, Mockito.times(0)).sendMessage(Mockito.anyString());
    }
}
