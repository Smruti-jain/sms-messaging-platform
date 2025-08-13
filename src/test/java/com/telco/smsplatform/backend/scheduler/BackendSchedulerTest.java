package com.telco.smsplatform.backend.scheduler;

import com.telco.smsplatform.backend.service.MessageProcessingService;
import com.telco.smsplatform.db.entity.SendMsgEntity;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.mockito.Mockito.*;

public class BackendSchedulerTest {

    @Test
    void testProcessMessages_Success() {
        // Prepare mock message
        SendMsgEntity msg = new SendMsgEntity();
        msg.setId(1L);
        msg.setMobile(9876543210L);
        msg.setMessage("Hello");
        msg.setAccountId(1001);
        msg.setStatus("NEW");

        MessageProcessingService service = Mockito.mock(MessageProcessingService.class);
        when(service.fetchNewMessages()).thenReturn(List.of(msg));

        BackendScheduler scheduler = new BackendScheduler(service);
        scheduler.processMessages();
        
        verify(service, times(1)).processMessage(msg);
    }

    @Test
    void testProcessMessages_NoNewMessages() {
        MessageProcessingService service = Mockito.mock(MessageProcessingService.class);
        when(service.fetchNewMessages()).thenReturn(List.of());

        BackendScheduler scheduler = new BackendScheduler(service);
        scheduler.processMessages();
        verify(service, never()).processMessage(any());
    }
}
