package backend.microservices.service.grpc;

import backend.microservices.entity.Account;
import backend.microservices.entity.enums.Blocked;
import backend.microservices.entity.enums.Deleted;
import backend.microservices.exception.NotFoundException;
import backend.microservices.repository.AccountRepository;
import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import main.proto.AccountServiceGrpc;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Service
@GrpcService
@RequiredArgsConstructor
public class AccountTransactionService extends AccountServiceGrpc.AccountServiceImplBase {

    private final AccountRepository accountRepository;

    @Override
    public void updateBalance(main.proto.TransactionUpdateBalance request, StreamObserver<Empty> responseObserver) {
        log.info("start update balance");
        Account account = accountRepository.findById(request.getId())
                .orElseThrow(() -> new NotFoundException("Аккаунт не найден!"));
        if (account.getDeleted().equals(Deleted.NOT_DELETED) && account.getBlocked().equals(Blocked.UNBLOCKED)) {
            Long amount = request.getAmount();
            account.setBalance(account.getBalance() + amount);
            accountRepository.save(account);
            log.info("end update balance");
            responseObserver.onNext(Empty.getDefaultInstance());
        }
        responseObserver.onCompleted();
    }

    @Override
    public void descendingBalance(main.proto.TransactionDescendingBalance request, StreamObserver<Empty> responseObserver) {
        log.info("start descending balance");
        Account account = accountRepository.findById(request.getId())
                .orElseThrow(() -> new NotFoundException("Аккаунт не найден"));
        Long id = request.getId();
        if (account.getDeleted().equals(Deleted.NOT_DELETED) && account.getBlocked().equals(Blocked.UNBLOCKED)) {
            Long amount = request.getAmount();
            account.setBalance(account.getBalance() - amount);
            accountRepository.save(account);
            log.info("end descending balance");
            responseObserver.onNext(Empty.getDefaultInstance());
        }
        responseObserver.onCompleted();
    }
}
