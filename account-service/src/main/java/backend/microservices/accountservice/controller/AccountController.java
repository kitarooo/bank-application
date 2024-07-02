package backend.microservices.accountservice.controller;

import backend.microservices.accountservice.dto.request.AccountRequest;
import backend.microservices.accountservice.dto.request.AccountUpdateBalanceRepost;
import backend.microservices.accountservice.dto.request.AccountUpdateRequest;
import backend.microservices.accountservice.dto.response.AccountFullResponse;
import backend.microservices.accountservice.entity.Account;
import backend.microservices.accountservice.service.impl.AccountServiceImpl;
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
    public AccountFullResponse getAccountById(@RequestHeader("Authorization") String token,
                                              @PathVariable Long id) {
        return accountService.getAccount(token,id);
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
}
