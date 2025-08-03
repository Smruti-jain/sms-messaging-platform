package com.telco.smsplatform.db.controller;

import com.telco.smsplatform.db.service.InternalDbService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/db")
public class InternalDbController {

    private final InternalDbService internalDbService;

    public InternalDbController(InternalDbService internalDbService) {
        this.internalDbService = internalDbService;
    }

    // GET ACCOUNT ID API
    @GetMapping("/account")
    public ResponseEntity<Integer> getAccountId(@RequestParam String username,
                                                @RequestParam String password) {
        Integer accountId = internalDbService.validateUser(username, password);
        if (accountId != null) {
            return ResponseEntity.ok(accountId);
        }
        return ResponseEntity.badRequest().build();
    }

    // INSERT SEND MSG API
    @PostMapping("/sendmsg")
    public ResponseEntity<String> insertSendMsg(@RequestParam Long mobile,
                                                @RequestParam String message,
                                                @RequestParam Integer accountId) {
        internalDbService.insertSendMsg(mobile, message, accountId);
        return ResponseEntity.ok("Inserted into send_msg table");
    }
}
