package backend.microservices.dto.request;

import backend.microservices.entity.enums.Currency;

public record AccountUpdateRequest(Currency currency, String accountNumber) {
}
