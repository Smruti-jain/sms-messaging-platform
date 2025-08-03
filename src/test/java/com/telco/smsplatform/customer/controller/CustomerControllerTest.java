package com.telco.smsplatform.customer.controller;

import com.telco.smsplatform.customer.model.CustomerResponse;
import com.telco.smsplatform.customer.service.CustomerService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CustomerController.class)
public class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerService customerService;

    @Test
    void testSendMessage_Success() throws Exception {
        Mockito.when(customerService.processMessage(any()))
                .thenReturn(new CustomerResponse("ACCEPTED", "SUCCESS", "ACK123"));

        mockMvc.perform(get("/telco/sendmsg")
                        .param("username", "user1")
                        .param("password", "password1")
                        .param("mobile", "9876543210")
                        .param("message", "Hello"))
                .andExpect(status().isOk());
    }
}

