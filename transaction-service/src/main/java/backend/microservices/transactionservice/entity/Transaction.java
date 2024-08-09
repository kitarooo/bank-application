package backend.microservices.transactionservice.entity;

import backend.microservices.transactionservice.entity.enums.Currency;
import backend.microservices.transactionservice.entity.enums.TransactionType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Builder
@Table(name = "transactions")
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    Long accountFrom;
    Long accountTo;
    BigDecimal amount;
    @Enumerated(EnumType.STRING)
    Currency currency;
    @Enumerated(EnumType.STRING)
    TransactionType transactionType;
    LocalDateTime transferTime;
    String description;

}
