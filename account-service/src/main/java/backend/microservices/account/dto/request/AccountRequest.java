package backend.microservices.account.dto.request;

import backend.microservices.account.entity.enums.Currency;
import lombok.Builder;

@Builder
public record AccountRequest(String accountNumber, Currency currency, String email) {
}
