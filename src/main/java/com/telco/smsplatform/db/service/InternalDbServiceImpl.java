package com.telco.smsplatform.db.service;

import com.telco.smsplatform.db.entity.SendMsgEntity;
import com.telco.smsplatform.db.entity.UserEntity;
import com.telco.smsplatform.db.repository.SendMsgRepository;
import com.telco.smsplatform.db.repository.UserRepository;
import com.telco.smsplatform.db.service.InternalDbService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class InternalDbServiceImpl implements InternalDbService {

    private final UserRepository userRepository;
    private final SendMsgRepository sendMsgRepository;

    public InternalDbServiceImpl(UserRepository userRepository, SendMsgRepository sendMsgRepository) {
        this.userRepository = userRepository;
        this.sendMsgRepository = sendMsgRepository;
    }

    @Override
    public Integer validateUser(String username, String password) {
        return userRepository.findByUsernameAndPassword(username, password)
                .map(UserEntity::getAccountId)
                .orElse(null);
    }

    @Override
    public SendMsgEntity insertSendMsg(Long mobile, String message, Integer accountId) {
        SendMsgEntity sms = new SendMsgEntity();
        sms.setMobile(mobile);
        sms.setMessage(message);
        sms.setStatus("NEW");
        sms.setReceivedTs(LocalDateTime.now());
        sms.setAccountId(accountId);
        return sendMsgRepository.save(sms);
    }
}

