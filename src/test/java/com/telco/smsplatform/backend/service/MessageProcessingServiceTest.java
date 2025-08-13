package com.telco.smsplatform.backend.service;

import com.telco.smsplatform.db.entity.SendMsgEntity;
import com.telco.smsplatform.db.repository.SendMsgRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.web.client.RestTemplate;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MessageProcessingServiceTest {

    @Mock
    private SendMsgRepository sendMsgRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private MessageProcessingService messageProcessingService;

    private SendMsgEntity testMsg;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        testMsg = new SendMsgEntity();
        testMsg.setId(1L);
        testMsg.setAccountId(1001);
        testMsg.setMobile(9876543210L);
        testMsg.setMessage("Hello World");
        testMsg.setStatus("NEW");
    }

    @Test
    void fetchNewMessages_ShouldReturnList() {
        when(sendMsgRepository.findByStatus("NEW")).thenReturn(List.of(testMsg));
        List<SendMsgEntity> result = messageProcessingService.fetchNewMessages();

        assertEquals(1, result.size());
        assertEquals("NEW", result.get(0).getStatus());
        verify(sendMsgRepository, times(1)).findByStatus("NEW");
    }

    @Test
    void processMessage_ShouldUpdateStatusToSent_OnAcceptedResponse() {
        when(restTemplate.getForObject(anyString(), eq(String.class)))
                .thenReturn("STATUS: ACCEPTED~~RESPONSE_CODE: SUCCESS~~ACK123");
        messageProcessingService.processMessage(testMsg);

        assertEquals("SENT", testMsg.getStatus());
        assertNotNull(testMsg.getSentTs());
        verify(sendMsgRepository, atLeast(2)).save(testMsg);
    }

    @Test
    void processMessage_ShouldUpdateStatusToFailed_OnRejectedResponse() {
        when(restTemplate.getForObject(anyString(), eq(String.class)))
                .thenReturn("STATUS: REJECTED~~RESPONSE_CODE: FAILURE");
        messageProcessingService.processMessage(testMsg);

        assertEquals("FAILED", testMsg.getStatus());
        assertNotNull(testMsg.getSentTs());
        verify(sendMsgRepository, atLeast(2)).save(testMsg);
    }
}


