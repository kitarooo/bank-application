package backend.microservices.service;

import backend.microservices.account.kafka.event.AccountCreatedRequest;
import backend.microservices.account.kafka.event.UpdateBalanceRequest;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface NotificationService {
    @KafkaListener(topics = "account-placed")
    void successfullyCreatedAccountMessage(AccountCreatedRequest request);
    @KafkaListener(topics = "account-balance-updated")
    void successUpdateBalance(UpdateBalanceRequest request);
}
