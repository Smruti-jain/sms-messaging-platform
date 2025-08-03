package com.telco.smsplatform.telecom.service;

public interface TelecomService {
    String sendSms(Integer accountId, Long mobile, String message);
}
