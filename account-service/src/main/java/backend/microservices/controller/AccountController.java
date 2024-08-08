package backend.microservices.controller;

import backend.microservices.dto.request.AccountRequest;
import backend.microservices.dto.request.AccountUpdateBalanceRepost;
import backend.microservices.dto.request.AccountUpdateRequest;
import backend.microservices.dto.response.AccountBalance;
import backend.microservices.dto.response.AccountFullResponse;
import backend.microservices.entity.Account;
import backend.microservices.service.impl.AccountServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountServiceImpl accountService;

    @PostMapping("/create")
    public String createAccount(@RequestBody AccountRequest request,
                                @RequestHeader("Authorization") String token) {
        return accountService.createAccount(request,token);
    }

    @PatchMapping("/{id}")
    public AccountFullResponse updateAccountInfo(@RequestBody AccountUpdateRequest request,
                                                 @RequestHeader("Authorization") String token,
                                                 @PathVariable Long id) {
        return accountService.updateAccount(request,token, id);
    }

    @PatchMapping("/balance/{id}")
    public String updateBalance(@RequestBody AccountUpdateBalanceRepost money,
                                @RequestHeader("Authorization") String token,
                                @PathVariable Long id) {
        return accountService.updateBalance(token,money,id);
    }

    @GetMapping("{id}")
    public AccountFullResponse getAccountById(@PathVariable Long id) {
        return accountService.getAccount(id);
    }

    @GetMapping
    public List<Account> getAllByAccounts(@RequestHeader("Authorization") String token) {
        return accountService.getAllMyAccounts(token);
    }

    @DeleteMapping("/{id}")
    public String deleteAccountById(@RequestHeader("Authorization") String token,
                                    @PathVariable Long id) {
        return accountService.deleteAccount(id, token);
    }

    @GetMapping("/deleted")
    public List<Account> getDeletedAccounts(@RequestHeader("Authorization") String token) {
        return accountService.getDeletedAccounts(token);
    }

    @PostMapping("/recover/{id}")
    public String recoverAccount(@PathVariable Long id) {
        return accountService.recoverAccount(id);
    }

    @GetMapping("/balance/{accountNumber}")
    public AccountBalance getBalanceByUserId(@PathVariable String accountNumber) {
        return accountService.getBalanceByAccountNumber(accountNumber);
    }
}
