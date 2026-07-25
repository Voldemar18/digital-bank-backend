package com.digitalbank.account.service;

import com.digitalbank.account.model.entity.Currency;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class CurrencyConverterService {
    public BigDecimal convert(BigDecimal amount, Currency from, Currency to) {//из какой валюты в какую
        if (from.equals(to)) {
            return amount;
        }
        else if (from.equals(Currency.USD)) {
            switch (to) {
                case EUR:
                    return amount.multiply(BigDecimal.valueOf(0.87));
                case KZT:
                    return amount.multiply(BigDecimal.valueOf(475.07));
                case RUB:
                    return amount.multiply(BigDecimal.valueOf(78.03));
                default: throw new IllegalArgumentException("Unsupported target currency: " + to);
            }
        }
        else if (from.equals(Currency.EUR)) {
            switch (to) {
                case USD:
                    return amount.multiply(BigDecimal.valueOf(1.14));
                case KZT:
                    return amount.multiply(BigDecimal.valueOf(541.4));
                case RUB:
                    return amount.multiply(BigDecimal.valueOf(88.89));
                default: throw new IllegalArgumentException("Unsupported target currency: " + to);
            }
        }
        else if (from.equals(Currency.KZT)) {
            switch (to) {
                case USD:
                    return amount.multiply(BigDecimal.valueOf(0.002105));
                case EUR:
                    return amount.multiply(BigDecimal.valueOf(0.001847));
                case RUB:
                    return amount.multiply(BigDecimal.valueOf(0.1675));
                default: throw new IllegalArgumentException("Unsupported target currency: " + to);
            }
        }
        else if (from.equals(Currency.RUB)) {
            switch (to) {
                case USD:
                    return amount.multiply(BigDecimal.valueOf(0.012815));

                case EUR:
                    return amount.multiply(BigDecimal.valueOf(0.01125));

                case KZT:
                    return amount.multiply(BigDecimal.valueOf(5.97));
                default: throw new IllegalArgumentException("Unsupported target currency: " + to);

            }
        }
        throw new IllegalArgumentException("Unsupported source currency: " + from);
    }
}
