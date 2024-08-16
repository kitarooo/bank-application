package backend.microservices.transactionservice.controller;

import backend.microservices.transactionservice.dto.request.TransactionRequest;
import backend.microservices.transactionservice.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
public class TransactionalController {
    private final TransactionService transactionService;

    @PostMapping("/transfer")
    public String transfer(@RequestBody TransactionRequest request,
                           @RequestHeader("Authorization") String token) {
        return transactionService.transactionTransfer(token,request);
    }
}
