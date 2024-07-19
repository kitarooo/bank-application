package backend.microservices.service;

import backend.microservices.account.kafka.event.AccountCreatedRequest;
import backend.microservices.account.kafka.event.UpdateBalanceRequest;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface NotificationService {
    @KafkaListener(topicPartitions = @TopicPartition(topic = "account-event", partitions = "0"))
    void successfullyCreatedAccountMessage(AccountCreatedRequest request);
    @KafkaListener(topicPartitions = @TopicPartition(topic = "account-event", partitions = "1"))
    void successUpdateBalance(UpdateBalanceRequest request);
}
