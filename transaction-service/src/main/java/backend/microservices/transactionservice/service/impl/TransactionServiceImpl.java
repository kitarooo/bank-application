package backend.microservices.transactionservice.service.impl;

import backend.microservices.transactionservice.dto.request.TransactionRequest;
import backend.microservices.transactionservice.entity.Transaction;
import backend.microservices.transactionservice.repository.TransactionRepository;
import backend.microservices.transactionservice.service.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import main.proto.AccountServiceGrpc;
import main.proto.TransactionDescendingBalance;
import main.proto.TransactionUpdateBalance;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final AccountServiceGrpc.AccountServiceBlockingStub accountServiceStub;

    private final TransactionRepository transactionRepository;

    @Override
    @Transactional
    public String transactionTransfer(TransactionRequest request) {

            TransactionUpdateBalance transactionUpdateBalance = TransactionUpdateBalance.newBuilder()
                    .setId(request.getAccountTo())
                    .setAmount(request.getAmount())
                    .build();

            TransactionDescendingBalance transactionDescendingBalance = TransactionDescendingBalance.newBuilder()
                    .setId(request.getAccountFrom())
                    .setAmount(request.getAmount())
                    .build();

            accountServiceStub.descendingBalance(transactionDescendingBalance);
            accountServiceStub.updateBalance(transactionUpdateBalance);

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


    }
}


