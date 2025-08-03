package com.telco.smsplatform.telecom.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
public class TelecomServiceImpl implements TelecomService {

    @Override
    public String sendSms(Integer accountId, Long mobile, String message) {
        log.info("Sending SMS via Telecom API: {} -> {}", mobile, message);

        // Validation checks
        if (accountId == null || mobile == null || message == null ||
                message.trim().isEmpty() || String.valueOf(mobile).length() != 10) {

            log.warn("Telecom API REJECTED for mobile: {}" , mobile);
            return "STATUS: REJECTED~~RESPONSE_CODE: FAILURE";
        }

        // Success
        String ackId = UUID.randomUUID().toString();
        log.info("Telecom API ACCEPTED for mobile: {}" , mobile);
        return "STATUS: ACCEPTED~~RESPONSE_CODE: SUCCESS~~" + ackId;
    }
}

