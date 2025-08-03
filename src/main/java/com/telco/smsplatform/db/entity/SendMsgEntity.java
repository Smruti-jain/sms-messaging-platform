package com.telco.smsplatform.db.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "send_msg")
@Getter
@Setter
public class SendMsgEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long mobile;

    @Column(length = 160)
    private String message;

    private String status;

    @Column(name = "received_ts")
    private LocalDateTime receivedTs;

    @Column(name = "sent_ts")
    private LocalDateTime sentTs;

    @Column(name = "telco_response")
    private String telcoResponse;

    @Column(name = "account_id")
    private Integer accountId;
}

