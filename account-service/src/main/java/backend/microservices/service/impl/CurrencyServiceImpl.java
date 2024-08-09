package backend.microservices.service.impl;

import backend.microservices.service.CurrencyService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
@Service
public class CurrencyServiceImpl implements CurrencyService {
    @Override
    public BigDecimal mapTengeToRub(BigDecimal tenge) {
        return tenge.divide(new BigDecimal("5.45"), 2, RoundingMode.HALF_UP);
    }

    @Override
    public BigDecimal mapTengeToUsd(BigDecimal tenge) {
        return tenge.divide(new BigDecimal("473.94"), 2, RoundingMode.HALF_UP);
    }

    @Override
    public BigDecimal mapRubToTenge(BigDecimal rub) {
        return rub.multiply(new BigDecimal("5.45")).setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public BigDecimal mapRubToUsd(BigDecimal rub) {
        return rub.divide(new BigDecimal("87.0"), 2, RoundingMode.HALF_UP);
    }

    @Override
    public BigDecimal mapUsdToTenge(BigDecimal usd) {
        return usd.multiply(new BigDecimal("473.94")).setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public BigDecimal mapUsdToRub(BigDecimal usd) {
        return usd.multiply(new BigDecimal("87.0")).setScale(2, RoundingMode.HALF_UP); // умножение с указанием точности и режима округления
    }
}