package backend.microservices.account.service;

import java.math.BigDecimal;

public interface CurrencyService {
    BigDecimal mapTengeToRub(BigDecimal tenge);
    BigDecimal mapTengeToUsd(BigDecimal tenge);
    BigDecimal mapRubToTenge(BigDecimal rub);
    BigDecimal mapRubToUsd(BigDecimal rub);
    BigDecimal mapUsdToTenge(BigDecimal usd);
    BigDecimal mapUsdToRub(BigDecimal usd);
}
