package backend.microservices.dto.request;

import backend.microservices.entity.enums.Currency;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountUpdateRequest {
    Currency currency;
    String accountNumber;
}
