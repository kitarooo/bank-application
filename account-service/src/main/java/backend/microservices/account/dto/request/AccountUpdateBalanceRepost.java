package backend.microservices.account.dto.request;

import java.math.BigDecimal;

public record AccountUpdateBalanceRepost(BigDecimal money) {
}
