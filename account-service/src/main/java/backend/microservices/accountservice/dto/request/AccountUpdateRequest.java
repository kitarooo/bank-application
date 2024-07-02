package backend.microservices.accountservice.dto.request;

import backend.microservices.accountservice.entity.enums.Currency;

public record AccountUpdateRequest(Currency currency, String accountNumber) {
}
