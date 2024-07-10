package backend.microservices.account.service.impl;

import backend.microservices.account.dto.request.AccountRequest;
import backend.microservices.account.dto.response.AccountFullResponse;
import backend.microservices.account.entity.Account;
import backend.microservices.account.entity.enums.Blocked;
import backend.microservices.account.entity.enums.Deleted;
import backend.microservices.account.entity.enums.Status;
import backend.microservices.account.event.AccountCreatedRequest;
import backend.microservices.account.repository.AccountRepository;
import backend.microservices.account.service.AccountService;
import backend.microservices.account.dto.request.AccountUpdateBalanceRepost;
import backend.microservices.account.dto.request.AccountUpdateRequest;
import backend.microservices.account.exception.NotFoundException;
import backend.microservices.account.service.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final JwtService jwtService;
    private final AccountRepository accountRepository;
    private final KafkaTemplate<String, AccountCreatedRequest> kafkaTemplate;

    @Override
    public String createAccount(AccountRequest request, String token) {
        Long userId = jwtService.extractUserId(token);
        String email = jwtService.extractEmail(token);

        Account tempAccount = Account.builder()
                .accountNumber(request.accountNumber())
                .balance(BigDecimal.valueOf(0))
                .currency(request.currency())
                .createdAt(LocalDateTime.now())
                .userId(userId)
                .blocked(Blocked.UNBLOCKED)
                .status(Status.ACTIVE)
                .deleted(Deleted.NOT_DELETED)
                .build();
        Account account = new Account();
        account = accountRepository.save(tempAccount);

        AccountCreatedRequest createdRequest = new AccountCreatedRequest(request.accountNumber(),email);
        log.info("Start send message to broker {}", createdRequest);
        kafkaTemplate.send("account-placed", createdRequest);
        log.info("End message to broker {}", createdRequest);
        log.info("Account successfully created!");
        return "Вы успешно создали счет!";
    }

    @Override
    public AccountFullResponse updateAccount(AccountUpdateRequest request, String token, Long id) {
        Long userId = jwtService.extractUserId(token);

        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Аккаунт не найден!"));
        if (userId.equals(account.getUserId())) {
            account.setAccountNumber(request.accountNumber());
            account.setCurrency(request.currency());
            accountRepository.save(account);

            return AccountFullResponse.builder()
                    .accountNumber(account.getAccountNumber())
                    .currency(account.getCurrency())
                    .balance(account.getBalance())
                    .build();
        } else {
            throw new NotFoundException("Аккаунт не найден!");
        }
    }

    @Override
    public String updateBalance(String token, AccountUpdateBalanceRepost money, Long id) {
        Long userId = jwtService.extractUserId(token);

        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Аккаунт не найден!"));

        if (userId.equals(account.getUserId())) {
            account.setUpdatedAt(LocalDateTime.now());
            account.setBalance(account.getBalance().add(money.money()));
            accountRepository.save(account);
            //TODO kafka from notification-service
            return "Счет успешно пополнен!";
        } else {
            throw new NotFoundException("Аккаунт не найден!");
        }
    }

    @Override
    public AccountFullResponse getAccount(String token, Long id) {
        Long userId = jwtService.extractUserId(token);
        Account account = accountRepository.findByUserId(userId)
                .orElseThrow(() -> new NotFoundException("Аккаунт не найден!"));
    // add validated
        return AccountFullResponse.builder()
                .accountNumber(account.getAccountNumber())
                .currency(account.getCurrency())
                .balance(account.getBalance())
                .build();
    }

    @Override
    public List<Account> getAllMyAccounts(String token) {
        Long userId = jwtService.extractUserId(token);
        return accountRepository.findAllByUserId(userId);
    }

    @Override
    public String deleteAccount(Long id, String token) {
        Long userId = jwtService.extractUserId(token);
        Account account = accountRepository.findByUserId(userId)
                .orElseThrow(() -> new NotFoundException("Аккаунт не найден!"));
        account.setDeleted(Deleted.DELETED);
        account.setId(null);
        account.setBlocked(Blocked.ISBLOCKED);
        account.setStatus(Status.INACTIVE);
        account.setBalance(null);
        account.setCurrency(null);
        account.setCreatedAt(null);
        account.setUpdatedAt(null);
        return "Счет успешно удален!";
    }

    @Override
    public List<Account> getDeletedAccounts(String token) {
        Long userId = jwtService.extractUserId(token);
        List<Account> accounts =  accountRepository.findAllByUserId(userId);
        return accounts.stream()
                .filter(account -> account.getDeleted().equals(Deleted.DELETED))
                .toList();
    }


}
