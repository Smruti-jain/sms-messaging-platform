package com.telco.smsplatform.customer.model;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerRequest {

    @NotBlank(message = "Username is required")
    private String username;

    @NotBlank(message = "Password is required")
    private String password;

    @NotNull(message = "Mobile number is required")
    @Pattern(regexp = "\\d{10}", message = "Mobile must be 10 digits")
    private String mobile;

    @NotBlank(message = "Message is required")
    @Size(max = 160, message = "Message length must not exceed 160 characters")
    private String message;
}
