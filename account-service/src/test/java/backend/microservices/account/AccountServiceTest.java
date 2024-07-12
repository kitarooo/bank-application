package backend.microservices.account;

import backend.microservices.account.dto.request.AccountRequest;
import backend.microservices.account.entity.Account;
import backend.microservices.account.entity.enums.Blocked;
import backend.microservices.account.entity.enums.Currency;
import backend.microservices.account.entity.enums.Deleted;
import backend.microservices.account.entity.enums.Status;
import backend.microservices.account.event.AccountCreatedRequest;
import backend.microservices.account.exception.AccountAlreadyExistException;
import backend.microservices.account.repository.AccountRepository;
import backend.microservices.account.service.JwtService;
import backend.microservices.account.service.impl.AccountServiceImpl;
import backend.microservices.account.service.impl.CurrencyServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {

    // dependencies
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private JwtService jwtService;
    @Mock
    private KafkaTemplate<String, AccountCreatedRequest> kafkaTemplate;
    @InjectMocks
    private CurrencyServiceImpl currencyService;

    @InjectMocks
    private AccountServiceImpl accountService;

    // dto
    private AccountRequest accountRequest;
    private String token;
    private Long userId;
    private String email;
    private Account account;


    @BeforeEach
    public void setUp() {
        token = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJpZCI6MSwiZW1haWwiOiJhemFAZ21haWwuY29tIiwic3ViIjoiYXphIiwiaWF0IjoxNzIwNzI2NjY4LCJleHAiOjE3MjA3Mjc1Njh9.1XeBIxzLgxUTbXaIpTRvDBYWdN_h3_ZBRMw_R6wAzTc";
        email = "aza@gmail.com";
        userId = 1L;

        when(jwtService.extractEmail(token)).thenReturn(email);
        when(jwtService.extractUserId(token)).thenReturn(userId);

        accountRequest = AccountRequest.builder()
                .accountNumber("123456789013456")
                .currency(Currency.USD)
                .email(email)
                .build();
        account = Account.builder()
                .accountNumber("123456789013456")
                .balance(BigDecimal.valueOf(0))
                .currency(Currency.USD)
                .createdAt(LocalDateTime.now())
                .userId(userId)
                .blocked(Blocked.UNBLOCKED)
                .status(Status.ACTIVE)
                .deleted(Deleted.NOT_DELETED)
                .build();
    }

    @Test
    public void createAccountSuccessTest() {
        when(accountRepository.findAByAccountNumber(accountRequest.accountNumber())).thenReturn(Optional.empty());
        when(accountRepository.save(any(Account.class))).thenReturn(account);

        String result = accountService.createAccount(accountRequest, token);

        assertEquals("Вы успешно создали счет!", result);

        verify(jwtService).extractEmail(token);
        verify(jwtService).extractUserId(token);
        verify(accountRepository).findAByAccountNumber(accountRequest.accountNumber());
        verify(accountRepository).save(any(Account.class));
        verify(kafkaTemplate).send(eq("account-placed"), any(AccountCreatedRequest.class));
    }

    @Test
    public void createAccountErrorTest() {
        when(accountRepository.findAByAccountNumber(accountRequest.accountNumber())).thenReturn(Optional.of(new Account()));

        AccountAlreadyExistException exception = assertThrows(AccountAlreadyExistException.class, () -> {
            accountService.createAccount(accountRequest,token);
        });

        assertEquals("Счет с такими данными уже существует!", exception.getMessage());
    }
}
