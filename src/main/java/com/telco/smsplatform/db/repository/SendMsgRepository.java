package com.telco.smsplatform.db.repository;

import com.telco.smsplatform.db.entity.SendMsgEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SendMsgRepository extends JpaRepository<SendMsgEntity, Long> {
}

