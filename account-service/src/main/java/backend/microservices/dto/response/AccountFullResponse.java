package backend.microservices.dto.response;

import backend.microservices.entity.enums.Currency;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record AccountFullResponse(String accountNumber, Currency currency, BigDecimal balance) {
}
