package backend.microservices.dto.response;

import backend.microservices.entity.enums.Currency;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AccountFullResponse {
    String accountNumber;
    Currency currency;
    Long balance;
}
