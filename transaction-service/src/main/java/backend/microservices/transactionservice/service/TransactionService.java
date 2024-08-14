package backend.microservices.transactionservice.service;

import backend.microservices.transactionservice.dto.request.TransactionRequest;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Transactional(isolation = Isolation.SERIALIZABLE)
public interface TransactionService {

    String transactionTransfer(String token, TransactionRequest request);

}
