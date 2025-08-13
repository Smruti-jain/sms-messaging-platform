package com.telco.smsplatform.db.repository;

import com.telco.smsplatform.db.entity.SendMsgEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface SendMsgRepository extends JpaRepository<SendMsgEntity, Long> {

    List<SendMsgEntity> findByStatus(String status);

}

