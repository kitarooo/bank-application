package backend.microservices.service;

import backend.microservices.dto.request.AccountRequest;
import backend.microservices.dto.request.AccountUpdateBalanceRepost;
import backend.microservices.dto.request.AccountUpdateRequest;
import backend.microservices.dto.response.AccountBalance;
import backend.microservices.dto.response.AccountFullResponse;
import backend.microservices.entity.Account;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Transactional
public interface AccountService {
    String createAccount(AccountRequest request, String token);
    //@Transactional(isolation = Isolation.SERIALIZABLE)
    AccountFullResponse updateAccount(AccountUpdateRequest request, String token, Long id);
    //@Transactional(isolation = Isolation.SERIALIZABLE)
    String updateBalance(String token, AccountUpdateBalanceRepost money, Long id);
    //@Transactional(isolation = Isolation.SERIALIZABLE)
    AccountFullResponse getAccount(Long id);
    List<Account> getAllMyAccounts(String token);
    String deleteAccount(Long id,String token);
    List<Account> getDeletedAccounts(String token);
    //@Transactional(isolation = Isolation.SERIALIZABLE)
    String recoverAccount(Long id);
    AccountBalance getBalanceByAccountNumber(String accountNumber);
}
