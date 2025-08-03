package com.telco.smsplatform.db.service;

import com.telco.smsplatform.db.entity.UserEntity;
import com.telco.smsplatform.db.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class InternalDbServiceTest {

    @Test
    void testValidateUser_Success() {
        UserRepository mockRepo = Mockito.mock(UserRepository.class);
        UserEntity user = new UserEntity();
        user.setAccountId(1001);
        user.setUsername("user1");
        user.setPassword("password1");

        Mockito.when(mockRepo.findByUsernameAndPassword("user1", "password1"))
                .thenReturn(java.util.Optional.of(user));

        InternalDbService service = new InternalDbServiceImpl(mockRepo, null);
        Integer accountId = service.validateUser("user1", "password1");

        assertEquals(1001, accountId);
    }
}
