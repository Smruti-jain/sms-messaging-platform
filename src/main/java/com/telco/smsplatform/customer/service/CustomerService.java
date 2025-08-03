package com.telco.smsplatform.customer.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.telco.smsplatform.customer.model.CustomerRequest;
import com.telco.smsplatform.customer.model.CustomerResponse;

public interface CustomerService {

    /**
     * Validates the request, generates ACK ID, pushes to Kafka.
     * @param request CustomerRequest containing username, password, mobile, message.
     * @return CustomerResponse containing status, code, and ackId.
     * @throws JsonProcessingException if JSON conversion fails.
     */
    CustomerResponse processMessage(CustomerRequest request) throws JsonProcessingException;

}

