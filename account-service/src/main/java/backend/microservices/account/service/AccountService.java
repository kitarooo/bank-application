package backend.microservices.account.service;

import backend.microservices.account.dto.request.AccountRequest;
import backend.microservices.account.dto.response.AccountFullResponse;
import backend.microservices.account.entity.Account;
import backend.microservices.account.dto.request.AccountUpdateBalanceRepost;
import backend.microservices.account.dto.request.AccountUpdateRequest;
import jakarta.transaction.Transactional;

import java.math.BigDecimal;
import java.util.List;
@Transactional
public interface AccountService {
    String createAccount(AccountRequest request, String token);
    AccountFullResponse updateAccount(AccountUpdateRequest request, String token, Long id);
    String updateBalance(String token, AccountUpdateBalanceRepost money, Long id);
    AccountFullResponse getAccount(Long id);
    List<Account> getAllMyAccounts(String token);
    String deleteAccount(Long id,String token);
    List<Account> getDeletedAccounts(String token);
    String recoverAccount(Long id);
}
