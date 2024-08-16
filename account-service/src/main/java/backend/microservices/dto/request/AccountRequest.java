package backend.microservices.dto.request;

import backend.microservices.entity.enums.Currency;
import lombok.Builder;

@Builder
public record AccountRequest(String accountNumber, Currency currency, String email) {
}
