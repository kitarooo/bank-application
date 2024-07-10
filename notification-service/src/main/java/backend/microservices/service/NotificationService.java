package backend.microservices.service;

import backend.microservices.account.event.AccountCreatedRequest.AccountCreatedRequest;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface NotificationService {
    @KafkaListener(topics = "account-placed")
    void successfullyCreatedAccountMessage(AccountCreatedRequest request);
}
