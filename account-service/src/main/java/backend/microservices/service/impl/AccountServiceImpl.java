package backend.microservices.service.impl;

import backend.microservices.dto.request.AccountRequest;
import backend.microservices.dto.response.AccountBalance;
import backend.microservices.dto.response.AccountFullResponse;
import backend.microservices.entity.Account;
import backend.microservices.entity.enums.Blocked;
import backend.microservices.entity.enums.Currency;
import backend.microservices.entity.enums.Deleted;
import backend.microservices.entity.enums.Status;
import backend.microservices.exception.AccountAlreadyExistException;
import backend.microservices.account.kafka.event.AccountCreatedRequest;
import backend.microservices.account.kafka.event.UpdateBalanceRequest;
import backend.microservices.repository.AccountRepository;
import backend.microservices.service.AccountService;
import backend.microservices.dto.request.AccountUpdateBalanceRepost;
import backend.microservices.dto.request.AccountUpdateRequest;
import backend.microservices.exception.NotFoundException;
import backend.microservices.service.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final JwtService jwtService;
    private final AccountRepository accountRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final CurrencyServiceImpl currencyService;

    @Override
    public String createAccount(AccountRequest request, String token) {
        Long userId = jwtService.extractUserId(token);
        String email = jwtService.extractEmail(token);
        if (accountRepository.findAByAccountNumber(request.accountNumber()).isPresent()) {
            throw new AccountAlreadyExistException("Счет с такими данными уже существует!");
        }
        Account accountTemp = Account.builder()
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
        account = accountRepository.save(accountTemp);

        AccountCreatedRequest createdRequest = new AccountCreatedRequest(email,request.accountNumber());
        log.info("Start send message to broker {}", createdRequest);
        kafkaTemplate.send("account-event", 0, "create", createdRequest);
        log.info("End message to broker {}", createdRequest);
        log.info("Account successfully created!");

        return "Вы успешно создали счет!";
    }

    @Override
    public AccountFullResponse updateAccount(AccountUpdateRequest request, String token, Long id) {
        Long userId = jwtService.extractUserId(token);

        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Аккаунт не найден!"));
        if (userId.equals(account.getUserId()) && account.getDeleted().equals(Deleted.NOT_DELETED)) {
            if (request.currency().equals(Currency.USD) && account.getCurrency().equals(Currency.KZT)) {
                account.setBalance(currencyService.mapTengeToUsd(account.getBalance()));
            } else if (request.currency().equals(Currency.USD) && account.getCurrency().equals(Currency.RUB)) {
                account.setBalance(currencyService.mapRubToUsd(account.getBalance()));
            } else if (request.currency().equals(Currency.RUB) && account.getCurrency().equals(Currency.KZT)) {
                account.setBalance(currencyService.mapTengeToRub(account.getBalance()));
            } else if (request.currency().equals(Currency.RUB) && account.getCurrency().equals(Currency.USD)) {
                account.setBalance(currencyService.mapUsdToRub(account.getBalance()));
            } else if (request.currency().equals(Currency.KZT) && account.getCurrency().equals(Currency.USD)) {
                account.setBalance(currencyService.mapUsdToTenge(account.getBalance()));
            } else if (request.currency().equals(Currency.KZT) && account.getCurrency().equals(Currency.RUB)) {
                account.setBalance(currencyService.mapRubToTenge(account.getBalance()));
            }
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

        if (userId.equals(account.getUserId()) && account.getDeleted().equals(Deleted.NOT_DELETED)) {
            String email = jwtService.extractEmail(token);
            account.setUpdatedAt(LocalDateTime.now());
            account.setBalance(account.getBalance().add(money.money()));
            accountRepository.save(account);

            UpdateBalanceRequest updateBalanceRequest = new UpdateBalanceRequest(email,account.getAccountNumber());
            log.info("Start send message to broker {}", updateBalanceRequest);
            kafkaTemplate.send("account-event", 1, "update", updateBalanceRequest);
            log.info("End message to broker {}", updateBalanceRequest);
            log.info("Счет успешно пополнен");

            return "Счет успешно пополнен!";
        } else {
            throw new NotFoundException("Аккаунт не найден!");
        }
    }

    @Override
    public AccountFullResponse getAccount(Long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Аккаунт не найден!"));
        if (account.getDeleted().equals(Deleted.NOT_DELETED)) {
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
    public List<Account> getAllMyAccounts(String token) {
        Long userId = jwtService.extractUserId(token);
        List<Account> accounts = accountRepository.findAllByUserId(userId);
        return accounts.stream()
                .filter(account -> account.getDeleted().equals(Deleted.NOT_DELETED))
                .toList();
    }

    @Override
    public String deleteAccount(Long id, String token) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Аккаунт не найден!"));
        account.setDeleted(Deleted.DELETED);
        account.setBlocked(Blocked.ISBLOCKED);
        account.setStatus(Status.INACTIVE);
        account.setBalance(BigDecimal.ZERO);
        account.setUpdatedAt(null);

        accountRepository.save(account);
        return "Счет успешно удален!";
    }

    @Override
    public List<Account> getDeletedAccounts(String token) {
        Long userId = jwtService.extractUserId(token);
        List<Account> accounts =  accountRepository.findAllByUserId(userId);

        return accounts.stream()
                .filter(account ->
                        account.getDeleted().equals(Deleted.DELETED))
                .toList();
    }

    @Override
    public String recoverAccount(Long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Аккаунт не найден!"));
        account.setDeleted(Deleted.NOT_DELETED);
        account.setBlocked(Blocked.UNBLOCKED);
        account.setStatus(Status.ACTIVE);
        account.setBalance(BigDecimal.ZERO);
        account.setUpdatedAt(LocalDateTime.now());
        accountRepository.save(account);
        return "Аккаунт успешно восстановлен!";
    }

    @Override
    public AccountBalance getBalanceByAccountNumber(String accountNumber) {
        BigDecimal balance = accountRepository.findBalanceByAccountNumber(accountNumber);
        if (balance != null) {
            return AccountBalance.builder()
                    .balance(balance)
                    .build();
        } else {
            throw new NotFoundException("Счет не найден!");
        }
    }
}
