package backend.microservices.accountservice.dto.request;

import backend.microservices.accountservice.entity.enums.Currency;

public record AccountRequest(String accountNumber, Currency currency) {
}
