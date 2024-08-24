package backend.microservices.service.impl;

import backend.microservices.service.CurrencyService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
@Service
public class CurrencyServiceImpl implements CurrencyService {
    @Override
    public Long mapTengeToRub(Long tenge) {
        //return tenge.divide(new BigDecimal("5.45"), 2, RoundingMode.HALF_UP);
        return tenge * 5;
    }

    @Override
    public Long mapTengeToUsd(Long tenge) {
        //return tenge.divide(new BigDecimal("473.94"), 2, RoundingMode.HALF_UP);
        return tenge * 470;
    }

    @Override
    public Long mapRubToTenge(Long rub) {
        //return rub.multiply(new BigDecimal("5.45")).setScale(2, RoundingMode.HALF_UP);
        return rub / 5;
    }

    @Override
    public Long mapRubToUsd(Long rub) {
        //return rub.divide(new BigDecimal("87.0"), 2, RoundingMode.HALF_UP);
        return rub * 87;
    }

    @Override
    public Long mapUsdToTenge(Long usd) {
        //return usd.multiply(new BigDecimal("473.94")).setScale(2, RoundingMode.HALF_UP);
        return usd / 5;
    }

    @Override
    public Long mapUsdToRub(Long usd) {
        //return usd.multiply(new BigDecimal("87.0")).setScale(2, RoundingMode.HALF_UP); // умножение с указанием точности и режима округления
        return usd / 87;
    }
}
