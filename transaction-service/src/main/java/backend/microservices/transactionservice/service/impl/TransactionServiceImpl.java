package backend.microservices.transactionservice.service.impl;

import backend.microservices.transactionservice.dto.request.TransactionRequest;
import backend.microservices.transactionservice.entity.Transaction;
import backend.microservices.transactionservice.grpc.BigDecimalConverter;
import backend.microservices.transactionservice.repository.TransactionRepository;
import backend.microservices.transactionservice.service.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import main.proto.AccountServiceGrpc;
import main.proto.TransactionDescendingBalance;
import main.proto.TransactionUpdateBalance;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    @GrpcClient("accountService")
    private AccountServiceGrpc.AccountServiceBlockingStub accountServiceStub;

    private final TransactionRepository transactionRepository;

    @Override
    public String transactionTransfer(String token, TransactionRequest request) {
        TransactionUpdateBalance transactionUpdateBalance = TransactionUpdateBalance.newBuilder()
                .setId(request.getAccountTo())
                .setAmount(BigDecimalConverter.toProto(request.getAmount()))
                .build();

        TransactionDescendingBalance transactionDescendingBalance = TransactionDescendingBalance.newBuilder()
                .setAmount(BigDecimalConverter.toProto(request.getAmount()))
                .setId(request.getAccountTo())
                .setToken(token)
                .build();

        accountServiceStub.updateBalance(transactionUpdateBalance);
        accountServiceStub.descendingBalance(transactionDescendingBalance);

        Transaction transaction = Transaction.builder()
                .accountTo(request.getAccountTo())
                .accountFrom(request.getAccountFrom())
                .transactionType(request.getTransactionType())
                .amount(request.getAmount())
                .description(request.getDescription())
                .build();
        transactionRepository.save(transaction);

        return "Вы успешно совершили перевод!";
    }
}

