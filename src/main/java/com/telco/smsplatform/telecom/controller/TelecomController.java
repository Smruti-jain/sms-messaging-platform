package com.telco.smsplatform.telecom.controller;

import com.telco.smsplatform.telecom.service.TelecomService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/telco")
public class TelecomController {

    private final TelecomService telecomService;

    public TelecomController(TelecomService telecomService) {
        this.telecomService = telecomService;
    }

    @GetMapping("/smsc")
    public String sendSms(@RequestParam("account_id") Integer accountId,
                          @RequestParam("mobile") Long mobile,
                          @RequestParam("message") String message) {
        return telecomService.sendSms(accountId, mobile, message);
    }
}

