package backend.microservices.entity;

import backend.microservices.entity.enums.Blocked;
import backend.microservices.entity.enums.Deleted;
import backend.microservices.entity.enums.Status;
import backend.microservices.entity.enums.Currency;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@Entity
@Table(name = "accounts")
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "user_id")
    Long userId;

    @Length(max = 16)
    @Column(name = "account_number")
    String accountNumber;

    @Column(name = "balance")
    BigDecimal balance = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    Currency currency;

    @Column(name = "created_at")
    LocalDateTime createdAt;

    @Column(name = "updated_at")
    LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    Blocked blocked;

    @Enumerated(EnumType.STRING)
    Status status;

    @Enumerated(EnumType.STRING)
    Deleted deleted;
}
