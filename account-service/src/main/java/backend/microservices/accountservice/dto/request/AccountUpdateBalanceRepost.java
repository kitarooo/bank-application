package backend.microservices.accountservice.dto.request;

import java.math.BigDecimal;

public record AccountUpdateBalanceRepost(BigDecimal money) {
}
