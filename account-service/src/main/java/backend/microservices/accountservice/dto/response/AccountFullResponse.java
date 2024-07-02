package backend.microservices.accountservice.dto.response;

import backend.microservices.accountservice.entity.enums.Currency;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record AccountFullResponse(String accountNumber, Currency currency, BigDecimal balance) {
}
