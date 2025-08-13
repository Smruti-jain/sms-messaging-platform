package com.telco.smsplatform.backend.service;

import com.telco.smsplatform.db.entity.SendMsgEntity;
import com.telco.smsplatform.db.repository.SendMsgRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class MessageProcessingService {

    private final SendMsgRepository sendMsgRepository;
    private final RestTemplate restTemplate;

    public MessageProcessingService(SendMsgRepository sendMsgRepository, RestTemplate restTemplate) {
        this.sendMsgRepository = sendMsgRepository;
        this.restTemplate = restTemplate;
    }

    public List<SendMsgEntity> fetchNewMessages() {
        return sendMsgRepository.findByStatus("NEW");
    }

    public void processMessage(SendMsgEntity msg) {
        markInProcess(msg);
        String response = sendToTelecom(msg);
        updateMessageStatus(msg, response);
    }

    private void markInProcess(SendMsgEntity msg) {
        msg.setStatus("INPROCESS");
        sendMsgRepository.save(msg);
    }

    private String sendToTelecom(SendMsgEntity msg) {
        try {
            String encodedMessage = URLEncoder.encode(msg.getMessage(), StandardCharsets.UTF_8);
            String telecomUrl = String.format(
                    "http://localhost:8080/telco/smsc?account_id=%d&mobile=%d&message=%s",
                    msg.getAccountId(),
                    msg.getMobile(),
                    encodedMessage
            );
            return restTemplate.getForObject(telecomUrl, String.class);

        } catch (RestClientException ex) {
            log.error("An error occurred while sending message to Telecom API: {}", ex.getMessage());
            throw new RuntimeException("Telecom API error: " + ex.getMessage());
        } catch (IllegalArgumentException ex) {
            log.error("Invalid argument: {}", ex.getMessage());
            throw new RuntimeException("Invalid message data: " + ex.getMessage());
        }
    }

    private void updateMessageStatus(SendMsgEntity msg, String response) {
        msg.setTelcoResponse(response);
        msg.setSentTs(LocalDateTime.now());

        if (response != null && response.contains("STATUS: ACCEPTED")) {
            msg.setStatus("SENT");
        } else {
            msg.setStatus("FAILED");
        }
        sendMsgRepository.save(msg);
    }
}
