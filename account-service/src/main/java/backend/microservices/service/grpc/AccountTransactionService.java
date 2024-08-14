package backend.microservices.service.grpc;

import backend.microservices.entity.Account;
import backend.microservices.entity.enums.Blocked;
import backend.microservices.entity.enums.Deleted;
import backend.microservices.exception.NotFoundException;
import backend.microservices.grpc.dto.request.TransactionDescendingBalance;
import backend.microservices.grpc.dto.request.TransactionUpdateBalance;
import backend.microservices.repository.AccountRepository;
import backend.microservices.service.JwtService;
import com.google.protobuf.Empty;
import io.grpc.Server;
import io.grpc.health.v1.HealthCheckResponse;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import main.proto.AccountServiceGrpc;
import org.lognet.springboot.grpc.GRpcService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;

@Slf4j
@Service
@GRpcService
@RequiredArgsConstructor
public class AccountTransactionService extends AccountServiceGrpc.AccountServiceImplBase {

    private final AccountRepository accountRepository;
    private final JwtService jwtService;

    @Override
    public void updateBalance(main.proto.TransactionUpdateBalance request, StreamObserver<Empty> responseObserver) {
        log.info("start update balance");
        Account account = accountRepository.findById(request.getId())
                .orElseThrow(() -> new NotFoundException("Аккаунт не найден!"));
        if (account.getDeleted().equals(Deleted.NOT_DELETED) && account.getBlocked().equals(Blocked.UNBLOCKED)) {
            BigDecimal amount = BigDecimalConverter.fromProto(request.getAmount());
            account.setBalance(account.getBalance().add(amount));
            accountRepository.save(account);
            responseObserver.onCompleted();
            log.info("end update balance");
        }
    }

    @Override
    public void descendingBalance(main.proto.TransactionDescendingBalance request, StreamObserver<Empty> responseObserver) {
        log.info("start descending balance");
        Account account = accountRepository.findById(request.getId())
                .orElseThrow(() -> new NotFoundException("Аккаунт не найден"));
        Long userId = jwtService.extractUserId(request.getToken());
        if (userId.equals(account.getUserId()) && account.getDeleted().equals(Deleted.NOT_DELETED)) {
            BigDecimal amount = BigDecimalConverter.fromProto(request.getAmount());
            account.setBalance(account.getBalance().subtract(amount));
            accountRepository.save(account);
            responseObserver.onCompleted();
            log.info("end descending balance");
        }
    }
}
