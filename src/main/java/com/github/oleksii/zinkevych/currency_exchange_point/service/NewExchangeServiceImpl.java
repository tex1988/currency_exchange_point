package com.github.oleksii.zinkevych.currency_exchange_point.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.github.oleksii.zinkevych.currency_exchange_point.constant.ExchangeMode;
import com.github.oleksii.zinkevych.currency_exchange_point.entity.ExchangeRate;
import com.github.oleksii.zinkevych.currency_exchange_point.repository.ExchangeRateRepository;
import com.github.oleksii.zinkevych.currency_exchange_point.service.common.BankService;
import com.github.oleksii.zinkevych.currency_exchange_point.service.common.ExchangeService;
import com.github.oleksii.zinkevych.currency_exchange_point.service.exception.ExchangeServiceNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

@Slf4j
@Service
@AllArgsConstructor
public class NewExchangeServiceImpl implements ExchangeService {

    private static final String EXCHANGE_NOT_FOUND = "Exchange rate from '%s' to '%s' not found";
    private static final String UAH = "UAH";
    private static final RoundingMode HALF_UP_ROUNDING_MODE = RoundingMode.HALF_UP;

    private final ExchangeRateRepository exchangeRateRepository;
    private final BankService bankService;

    @Override
    public BigDecimal calculateExchange(String saleCurrency, String purchaseCurrency,
                                        BigDecimal purchaseAmount, ExchangeMode exchangeMode, String proxyCurrency) {
        List<ExchangeRate> exchangeRates = getExchangeRates();
        BigDecimal dealAmount = null;
        switch (exchangeMode) {
            case DIRECT -> dealAmount = calculateDirect(saleCurrency, purchaseCurrency, purchaseAmount, exchangeRates);
            case PROXY -> dealAmount = calculateProxy(saleCurrency, purchaseCurrency, purchaseAmount, exchangeRates, proxyCurrency);
        }
        return dealAmount;
    }

    private BigDecimal calculateDirect(String saleCurrency, String purchaseCurrency,
                                       BigDecimal purchaseAmount, List<ExchangeRate> exchangeRates) {
        BigDecimal saleRateOptional = getCurrencySaleRate(purchaseCurrency, saleCurrency, exchangeRates).orElseThrow(
            () -> new ExchangeServiceNotFoundException(String.format(EXCHANGE_NOT_FOUND, saleCurrency, purchaseCurrency)));
        return multiply(purchaseAmount, saleRateOptional);
    }

    private BigDecimal calculateProxy(String saleCurrency, String purchaseCurrency,
                                      BigDecimal purchaseAmount, List<ExchangeRate> exchangeRates, String proxyCurrency) {

        BigDecimal purchaseCurrencyInUAH = calculateForProxyCurrency(purchaseCurrency, proxyCurrency, purchaseAmount, exchangeRates);
        BigDecimal buyRateInUahForSaleCurrency = getCurrencyBuyRate(saleCurrency, proxyCurrency, exchangeRates)
            .orElseThrow(
                () -> new ExchangeServiceNotFoundException(String.format(EXCHANGE_NOT_FOUND, saleCurrency, proxyCurrency)));
        return divide(purchaseCurrencyInUAH, buyRateInUahForSaleCurrency);
    }

    private BigDecimal calculateForProxyCurrency(String saleCurrency, String proxyCurrency, BigDecimal purchaseAmount, List<ExchangeRate> exchangeRates) {
        Optional<BigDecimal> saleRateOptional;
        if (saleCurrency.equals(proxyCurrency)) {
            saleRateOptional = getCurrencySaleRate(proxyCurrency, saleCurrency, exchangeRates);
        } else {
            saleRateOptional = getCurrencySaleRate(saleCurrency, proxyCurrency, exchangeRates);
        }
        BigDecimal saleRate = saleRateOptional
            .orElseThrow(() -> new ExchangeServiceNotFoundException(EXCHANGE_NOT_FOUND));
        return multiply(purchaseAmount, saleRate);
    }

    private Optional<BigDecimal> getCurrencySaleRate(String currency, String baseCurrency, List<ExchangeRate> exchangeRates) {
        return getExchangeRate(currency, baseCurrency, exchangeRates).map(ExchangeRate::getSale);
    }

    private Optional<BigDecimal> getCurrencyBuyRate(String currency, String baseCurrency, List<ExchangeRate> exchangeRates) {
        return getExchangeRate(currency, baseCurrency, exchangeRates).map(ExchangeRate::getBuy);
    }

    private Optional<ExchangeRate> getExchangeRate(String currency, String baseCurrency, List<ExchangeRate> exchangeRates) {
        return Optional.ofNullable(exchangeRates
            .stream()
            .filter(er -> er.getCcy().equals(currency) && er.getBase_ccy().equals(baseCurrency))
            .findFirst()
            .orElse(getExchangeRate(currency, baseCurrency)));
    }

    private ExchangeRate getExchangeRate(String currency, String baseCurrency) {
        return exchangeRateRepository
            .findLastByCcyAndBaseCcyInDateRange(currency, baseCurrency, getStartOfToday(), getEndOfToday());
    }

    private LocalDateTime getStartOfToday() {
        return LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT);
    }

    private LocalDateTime getEndOfToday() {
        return LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
    }

    private List<ExchangeRate> getExchangeRates() {
        try {
            return bankService.getExchangeRates();
        } catch (HttpClientErrorException e) {
            log.warn("Banks service error, ", e);
            return Collections.emptyList();
        }
    }

    private BigDecimal multiply(BigDecimal multiplier, BigDecimal multiplicand) {
        return multiplier.multiply(multiplicand)
            .setScale(2, HALF_UP_ROUNDING_MODE);
    }

    private BigDecimal divide(BigDecimal dividend, BigDecimal divider) {
        return dividend.divide(divider, HALF_UP_ROUNDING_MODE)
            .setScale(2, HALF_UP_ROUNDING_MODE);
    }
}
