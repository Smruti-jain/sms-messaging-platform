package com.telco.smsplatform.customer.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CustomerResponse {

    private String status;
    private String responseCode;
    private String ackId;

}

