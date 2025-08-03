package com.telco.smsplatform.backend.scheduler;

import com.telco.smsplatform.db.entity.SendMsgEntity;
import com.telco.smsplatform.db.repository.SendMsgRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;

public class BackendSchedulerTest {

    @Test
    void testProcessMessages_Success() {
        SendMsgEntity msg = new SendMsgEntity();
        msg.setId(1L);
        msg.setMobile(9876543210L);
        msg.setMessage("Hello");
        msg.setAccountId(1001);
        msg.setStatus("NEW");

        SendMsgRepository repo = Mockito.mock(SendMsgRepository.class);
        Mockito.when(repo.findAll()).thenReturn(List.of(msg));

        RestTemplate restTemplate = Mockito.mock(RestTemplate.class);
        Mockito.when(restTemplate.getForObject(anyString(), Mockito.eq(String.class)))
                .thenReturn("STATUS: ACCEPTED~~RESPONSE_CODE: SUCCESS~~ACK");

        BackendScheduler scheduler = new BackendScheduler(repo);
        scheduler.processMessages();

        Mockito.verify(repo, Mockito.atLeastOnce()).save(msg);
    }
}

