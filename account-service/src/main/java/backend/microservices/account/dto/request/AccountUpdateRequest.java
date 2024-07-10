package backend.microservices.account.dto.request;

import backend.microservices.account.entity.enums.Currency;

public record AccountUpdateRequest(Currency currency, String accountNumber) {
}
