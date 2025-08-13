package com.telco.smsplatform.backend.scheduler;

import com.telco.smsplatform.backend.service.MessageProcessingService;
import com.telco.smsplatform.db.entity.SendMsgEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
public class BackendScheduler {

    private final MessageProcessingService messageProcessingService;

    public BackendScheduler(MessageProcessingService messageProcessingService) {
        this.messageProcessingService = messageProcessingService;
    }

    @Scheduled(fixedRate = 1000)
    public void processMessages() {
        List<SendMsgEntity> newMessages = messageProcessingService.fetchNewMessages();
        if (newMessages.isEmpty()) {
            log.info("No NEW messages found at {}", LocalDateTime.now());
            return;
        }
        log.info("Found {} NEW messages to process", newMessages.size());
        newMessages.forEach(msg -> {
            try {
                messageProcessingService.processMessage(msg);
            } catch (Exception e) {
                log.error("Error processing message {}: {}", msg.getId(), e.getMessage());
                throw e;
            }
        });
    }
}
