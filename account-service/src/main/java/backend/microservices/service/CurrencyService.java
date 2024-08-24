package backend.microservices.service;

import java.math.BigDecimal;

public interface CurrencyService {
    Long mapTengeToRub(Long tenge);
    Long mapTengeToUsd(Long tenge);
    Long mapRubToTenge(Long rub);
    Long mapRubToUsd(Long rub);
    Long mapUsdToTenge(Long usd);
    Long mapUsdToRub(Long usd);
}
