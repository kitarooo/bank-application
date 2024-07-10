package backend.microservices.account.dto.response;

import backend.microservices.account.entity.enums.Currency;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record AccountFullResponse(String accountNumber, Currency currency, BigDecimal balance) {
}
