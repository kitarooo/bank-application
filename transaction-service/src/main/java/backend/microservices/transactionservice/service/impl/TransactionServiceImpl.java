package backend.microservices.transactionservice.service.impl;

import backend.microservices.transactionservice.dto.request.CreateTransactionRequest;
import backend.microservices.transactionservice.kafka.event.TransactionDescendingBalance;
import backend.microservices.transactionservice.entity.Transaction;
import backend.microservices.transactionservice.kafka.event.TransactionUpdateBalance;
import backend.microservices.transactionservice.repository.TransactionRepository;
import backend.microservices.transactionservice.service.JwtService;
import backend.microservices.transactionservice.service.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final JwtService jwtService;

    @Override
    public String transactionTransfer(String token, CreateTransactionRequest createTransactionRequest) {
        log.info("start transaction transfer... {}", createTransactionRequest);
        Transaction transaction = new Transaction();

        // Send to kafka topic for update balance
        TransactionUpdateBalance updateBalanceRequest = new TransactionUpdateBalance();
        updateBalanceRequest.setId(createTransactionRequest.getAccountTo());
        updateBalanceRequest.setAmount(createTransactionRequest.getAmount());
        kafkaTemplate.send("transaction-event", 0, "update",updateBalanceRequest);

        // Send to kafka topic for descending balance
        TransactionDescendingBalance descendingBalanceRequest = new TransactionDescendingBalance();
        descendingBalanceRequest.setId(createTransactionRequest.getAccountFrom());
        descendingBalanceRequest.setToken(token);
        descendingBalanceRequest.setAmount(createTransactionRequest.getAmount());
        kafkaTemplate.send("transaction-event", 1, "descending",descendingBalanceRequest);
        log.info("end transaction transfer... {}", createTransactionRequest);
        transaction.setAccountTo(createTransactionRequest.getAccountTo());
        transaction.setAmount(createTransactionRequest.getAmount());
        transaction.setTransactionType(createTransactionRequest.getTransactionType());
        transaction.setDescription(createTransactionRequest.getDescription());
        transaction.setTransferTime(LocalDateTime.now());
        transactionRepository.save(transaction);

        return "Перевод успешно выполнен!";
    }
}
