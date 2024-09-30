package backend.microservices.transactionservice.service.impl;

import backend.microservices.transactionservice.dto.request.TransactionRequest;
import backend.microservices.transactionservice.entity.Transaction;
import backend.microservices.transactionservice.repository.TransactionRepository;
import backend.microservices.transactionservice.service.JwtService;
import backend.microservices.transactionservice.service.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import main.proto.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final AccountServiceGrpc.AccountServiceBlockingStub accountServiceStub;
    private final TransactionRepository transactionRepository;
    private final JwtService jwtService;

    @Override
    @Transactional
    public String transactionTransfer(String token, TransactionRequest request) {

            // transfer account -> to
            TransactionUpdateBalance transactionUpdateBalance = TransactionUpdateBalance.newBuilder()
                    .setId(request.getAccountTo())
                    .setAmount(request.getAmount())
                    .build();

            // descending balance <- from
            Long accountFrom = jwtService.extractUserId(token);
            TransactionDescendingBalance transactionDescendingBalance = TransactionDescendingBalance.newBuilder()
                    .setId(accountFrom)
                    .setAmount(request.getAmount())
                    .build();

        BalanceCheckRequest balanceCheckRequest = BalanceCheckRequest.newBuilder()
                .setId(accountFrom)
                .build();

        BalanceCheckResponse balanceCheckResponse = accountServiceStub.checkBalance(balanceCheckRequest);

        if (balanceCheckResponse.getSuccess()) {
            Thread thread1 = new Thread(new Runnable() {
                @Override
                public void run() {
                    accountServiceStub.descendingBalance(transactionDescendingBalance);
                }
            });

            Thread thread2 = new Thread(new Runnable() {
                @Override
                public void run() {
                    accountServiceStub.updateBalance(transactionUpdateBalance);
                }
            });

            thread1.start();
            thread2.start();
            Transaction transaction = Transaction.builder()
                    .accountTo(request.getAccountTo())
                    .accountFrom(request.getAccountFrom())
                    .transactionType(request.getTransactionType())
                    .amount(request.getAmount())
                    .description(request.getDescription())
                    .build();

            transactionRepository.save(transaction);

            log.info("Transaction successfully processed: {}", transaction);
            return "Вы успешно совершили перевод!";
        } else {
            
        }



    }
}


