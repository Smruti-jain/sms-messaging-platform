package com.telco.smsplatform.backend.scheduler;

import com.telco.smsplatform.db.entity.SendMsgEntity;
import com.telco.smsplatform.db.repository.SendMsgRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
public class BackendScheduler {

    private final SendMsgRepository sendMsgRepository;
    private final RestTemplate restTemplate;

    public BackendScheduler(SendMsgRepository sendMsgRepository) {
        this.sendMsgRepository = sendMsgRepository;
        this.restTemplate = new RestTemplate();
    }

    @Scheduled(fixedRate = 1000) // Every 1 second
    public void processMessages() {
        // Fetch messages with status = NEW
        List<SendMsgEntity> newMessages = sendMsgRepository.findAll()
                .stream()
                .filter(msg -> "NEW".equalsIgnoreCase(msg.getStatus()))
                .toList();

        if (newMessages.isEmpty()) {
            log.info("No NEW messages found at {}", LocalDateTime.now());
            return;
        }

        log.info("Found {} NEW messages to process", newMessages.size());
        for (SendMsgEntity msg : newMessages) {
            try {
                // Mark as INPROCESS
                msg.setStatus("INPROCESS");
                log.info("Processing message ID: {} Mobile: {}", msg.getId(), msg.getMobile());
                sendMsgRepository.save(msg);

                // Call Telecom API
                String telecomUrl = String.format(
                        "http://localhost:8080/telco/smsc?account_id=%d&mobile=%d&message=%s",
                        msg.getAccountId(),
                        msg.getMobile(),
                        msg.getMessage()
                );

                String response = restTemplate.getForObject(telecomUrl, String.class);

                // Update status & response
                msg.setTelcoResponse(response);
                if (response != null && response.contains("STATUS: ACCEPTED")) {
                    msg.setStatus("SENT");
                } else {
                    msg.setStatus("FAILED");
                }
                msg.setSentTs(LocalDateTime.now());
                sendMsgRepository.save(msg);

                log.info("Message sent to Telecom API: {}", msg.getId());

            } catch (Exception e) {
                msg.setStatus("FAILED");
                msg.setTelcoResponse("Error: " + e.getMessage());
                sendMsgRepository.save(msg);
                log.warn("Error sending message ID {}: {}" , msg.getId(), e.getMessage());
            }
        }
    }
}

