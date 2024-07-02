package backend.microservices.accountservice.service.impl;

import backend.microservices.accountservice.dto.request.AccountRequest;
import backend.microservices.accountservice.dto.request.AccountUpdateBalanceRepost;
import backend.microservices.accountservice.dto.request.AccountUpdateRequest;
import backend.microservices.accountservice.dto.response.AccountFullResponse;
import backend.microservices.accountservice.entity.Account;
import backend.microservices.accountservice.exception.NotFoundException;
import backend.microservices.accountservice.repository.AccountRepository;
import backend.microservices.accountservice.service.AccountService;
import backend.microservices.accountservice.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final JwtService jwtService;
    private final AccountRepository accountRepository;

    @Override
    public String createAccount(AccountRequest request, String token) {
        Long userId = jwtService.extractUserId(token);

        Account account = Account.builder()
                .accountNumber(request.accountNumber())
                .balance(BigDecimal.valueOf(0))
                .currency(request.currency())
                .createdAt(LocalDateTime.now())
                .userId(userId)
                .build();

        accountRepository.save(account);

        // TODO add kafka and notification-service
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
        accountRepository.delete(account);
        return "Счет успешно удален!";
    }
}
