package backend.microservices.dto.request;

import java.math.BigDecimal;

public record AccountUpdateBalanceRepost(BigDecimal money) {
}
