package backend.microservices.transactionservice.service;

import backend.microservices.transactionservice.dto.request.CreateTransactionRequest;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Transactional(isolation = Isolation.SERIALIZABLE)
public interface TransactionService {

    String transactionTransfer(String token, CreateTransactionRequest createTransactionRequest);

}
