package backend.microservices.accountservice.service;

import backend.microservices.accountservice.dto.request.AccountRequest;
import backend.microservices.accountservice.dto.request.AccountUpdateBalanceRepost;
import backend.microservices.accountservice.dto.request.AccountUpdateRequest;
import backend.microservices.accountservice.dto.response.AccountFullResponse;
import backend.microservices.accountservice.dto.response.AccountResponse;
import backend.microservices.accountservice.entity.Account;

import java.math.BigDecimal;
import java.util.List;

public interface AccountService {
    String createAccount(AccountRequest request, String token);
    AccountFullResponse updateAccount(AccountUpdateRequest request, String token, Long id);
    String updateBalance(String token, AccountUpdateBalanceRepost money, Long id);
    AccountFullResponse getAccount(String token, Long id);
    List<Account> getAllMyAccounts(String token);
    String deleteAccount(Long id,String token);
}
