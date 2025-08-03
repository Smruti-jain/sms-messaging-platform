package com.telco.smsplatform.customer.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.telco.smsplatform.customer.model.CustomerRequest;
import com.telco.smsplatform.customer.model.CustomerResponse;
import com.telco.smsplatform.customer.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/telco")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/sendmsg")
    public ResponseEntity<CustomerResponse> sendMessage(@Valid @ModelAttribute CustomerRequest request) {
        try {
            CustomerResponse response = customerService.processMessage(request);
            return ResponseEntity.ok(response);
        } catch (JsonProcessingException e) {
            return ResponseEntity.internalServerError()
                    .body(new CustomerResponse("REJECTED", "FAILURE", null));
        }
    }
}

