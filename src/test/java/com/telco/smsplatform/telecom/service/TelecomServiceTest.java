package com.telco.smsplatform.telecom.service;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TelecomServiceTest {

    @Test
    void testSendSms_Success() {
        TelecomServiceImpl service = new TelecomServiceImpl();
        String response = service.sendSms(1001, 9876543210L, "Hello");

        assertTrue(response.contains("STATUS: ACCEPTED"));
    }

    @Test
    void testSendSms_Failure_InvalidMobile() {
        TelecomServiceImpl service = new TelecomServiceImpl();
        String response = service.sendSms(1001, 12345L, "Hello");

        assertTrue(response.contains("STATUS: REJECTED"));
    }
}

