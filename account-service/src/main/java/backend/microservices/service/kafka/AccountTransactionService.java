package backend.microservices.service.kafka;

import backend.microservices.kafka.event.TransactionDescendingBalance;
import backend.microservices.kafka.event.TransactionUpdateBalance;
import backend.microservices.entity.Account;
import backend.microservices.entity.enums.Blocked;
import backend.microservices.entity.enums.Deleted;
import backend.microservices.exception.NotFoundException;
import backend.microservices.repository.AccountRepository;
import backend.microservices.service.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountTransactionService {

    private final AccountRepository accountRepository;
    private final JwtService jwtService;

    @KafkaListener(topicPartitions = @TopicPartition(topic = "transaction-event", partitions = "1"))
    public void descendingBalance(String token, TransactionDescendingBalance request) {
        log.info("start descending balance");
        Account account = accountRepository.findById(request.getId())
                .orElseThrow(() -> new NotFoundException("Аккаунт не найден"));
        Long userId = jwtService.extractUserId(token);
        if (userId.equals(account.getUserId()) && account.getDeleted().equals(Deleted.NOT_DELETED)) {
            account.setBalance(account.getBalance().subtract(request.getAmount()));
            accountRepository.save(account);
        }
        log.info("end descending balance");
    }

    @KafkaListener(topicPartitions = @TopicPartition(topic = "transaction-event", partitions = "0"))
    public void updateBalance(TransactionUpdateBalance request) {
        log.info("start update balance");
        Account account = accountRepository.findById(request.getId())
                .orElseThrow(() -> new NotFoundException("Аккаунт не найден!"));
        if (account.getDeleted().equals(Deleted.NOT_DELETED) && account.getBlocked().equals(Blocked.UNBLOCKED)) {
            account.setBalance(account.getBalance().add(request.getAmount()));
            accountRepository.save(account);
            log.info("end update balance");
        }
    }
}
