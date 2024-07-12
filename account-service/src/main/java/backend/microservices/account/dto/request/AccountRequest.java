package backend.microservices.account.dto.request;

import backend.microservices.account.entity.enums.Currency;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Builder
public record AccountRequest(String accountNumber, Currency currency, String email) {
}
