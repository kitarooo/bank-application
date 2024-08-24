package backend.microservices.dto.request;

import backend.microservices.entity.enums.Currency;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AccountRequest {
    String accountNumber;
    Currency currency;
    String email;
}
