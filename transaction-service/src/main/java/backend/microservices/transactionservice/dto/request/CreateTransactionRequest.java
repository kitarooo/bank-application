package backend.microservices.transactionservice.dto.request;

import backend.microservices.transactionservice.entity.enums.TransactionType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateTransactionRequest {
    Long accountFrom;
    Long accountTo;
    BigDecimal amount;
    // TODO add DEPOSIT-SERVICE for different enums
    TransactionType transactionType;
    String description;
}