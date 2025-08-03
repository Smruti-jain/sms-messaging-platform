package com.telco.smsplatform.db.service;

import com.telco.smsplatform.db.entity.SendMsgEntity;

public interface InternalDbService {
    Integer validateUser(String username, String password);
    SendMsgEntity insertSendMsg(Long mobile, String message, Integer accountId);
}

