package com.github.oleksii.zinkevych.currency_exchange_point.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;

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
public class ExchangeServiceImpl implements ExchangeService {

    private static final String CURRENCY_NOT_FOUND = "Currency '%s' not found";
    private static final RoundingMode HALF_UP_ROUNDING_MODE = RoundingMode.HALF_UP;
    private static final String  BTC = "BTC";
    private static final String UAH = "UAH";
    private static final String USD = "USD";

    private final ExchangeRateRepository exchangeRateRepository;
    private final BankService bankService;

    @Override
    public BigDecimal calculateExchange(String saleCurrency, String purchaseCurrency,
                                        BigDecimal purchaseAmount, ExchangeMode exchangeMode, String proxyCurrency) {
        BigDecimal result;
        List<ExchangeRate> exchangeRates = getExchangeRates();
        if (purchaseCurrency.equals(BTC) || saleCurrency.equals(BTC)) {
            result = calculateForBtc(saleCurrency, purchaseCurrency, purchaseAmount, exchangeRates);
        } else {
            result = calculateForCurrency(saleCurrency, purchaseCurrency, purchaseAmount, exchangeRates);
        }
        return result;
    }

    private BigDecimal calculateForCurrency(String saleCurrency, String purchaseCurrency, BigDecimal purchaseAmount,
                                            List<ExchangeRate> exchangeRates) {
        BigDecimal result;
        if (purchaseCurrency.equals(UAH)) {
            result = getCurrencyAmountForUahSale(saleCurrency, purchaseAmount, exchangeRates)
                .setScale(2, HALF_UP_ROUNDING_MODE);
        } else if (saleCurrency.equals(UAH)) {
            result = getUahAmountForCurrencySale(purchaseCurrency, purchaseAmount, exchangeRates)
                .setScale(2, HALF_UP_ROUNDING_MODE);
        } else {
            BigDecimal purchaseCurrencyInUah = getUahAmountForCurrencySale(purchaseCurrency, purchaseAmount, exchangeRates);
            BigDecimal saleCurrencySaleRate = getCurrencySaleRate(saleCurrency, exchangeRates);
            result = purchaseCurrencyInUah.divide(saleCurrencySaleRate, 2, HALF_UP_ROUNDING_MODE);
        }
        return result;
    }

    private BigDecimal calculateForBtc(String saleCurrency, String purchaseCurrency, BigDecimal purchaseAmount,
                                       List<ExchangeRate> exchangeRates) {
        BigDecimal result;
        if (saleCurrency.equals(BTC) && purchaseCurrency.equals(USD)) {
            result = getUsdAmountForBtcSale(purchaseAmount, exchangeRates).setScale(2, HALF_UP_ROUNDING_MODE);
        } else if (saleCurrency.equals(USD) && purchaseCurrency.equals(BTC)) {
            result = getBtcAmountFromUsdSale(purchaseAmount, exchangeRates).setScale(2, HALF_UP_ROUNDING_MODE);
        } else if (saleCurrency.equals(BTC)) {
            BigDecimal buyBtcRate = getCurrencyBuyRate(BTC, exchangeRates);
            BigDecimal usdAmount = calculateForCurrency(USD, purchaseCurrency, purchaseAmount, exchangeRates);
            result = usdAmount.divide(buyBtcRate, 5, RoundingMode.HALF_UP)
                .setScale(2, HALF_UP_ROUNDING_MODE);
        } else {
            BigDecimal saleBtcRate = getCurrencySaleRate(BTC, exchangeRates);
            BigDecimal usdAmount = purchaseAmount.multiply(saleBtcRate);
            result = calculateForCurrency(saleCurrency, USD, usdAmount, exchangeRates)
                .setScale(2, HALF_UP_ROUNDING_MODE);
        }
        return result;
    }

    private BigDecimal getUahAmountForCurrencySale(String currency, BigDecimal currencyAmount, List<ExchangeRate> exchangeRates) {
        BigDecimal purchaseCurrencySaleRate = getCurrencySaleRate(currency, exchangeRates);
        return purchaseCurrencySaleRate.multiply(currencyAmount);
    }

    private BigDecimal getCurrencyAmountForUahSale(String currency, BigDecimal uahAmount, List<ExchangeRate> exchangeRates) {
        BigDecimal purchaseCurrencyBuyRate = getCurrencyBuyRate(currency, exchangeRates);
        return purchaseCurrencyBuyRate.divide(uahAmount, 5, HALF_UP_ROUNDING_MODE);
    }

    private BigDecimal getUsdAmountForBtcSale(BigDecimal purchaseAmount, List<ExchangeRate> exchangeRates) {
        BigDecimal buyBtcRate = getCurrencyBuyRate(BTC, exchangeRates);
        return purchaseAmount.divide(buyBtcRate, 5, HALF_UP_ROUNDING_MODE);
    }

    private BigDecimal getBtcAmountFromUsdSale(BigDecimal purchaseAmount, List<ExchangeRate> exchangeRates) {
        BigDecimal saleBtcRate = getCurrencySaleRate(BTC, exchangeRates);
        return purchaseAmount.multiply(saleBtcRate);
    }

    private BigDecimal getCurrencySaleRate(String currency, List<ExchangeRate> exchangeRates) {
        return getExchangeRate(currency, exchangeRates).getSale();
    }

    private BigDecimal getCurrencyBuyRate(String currency, List<ExchangeRate> exchangeRates) {
        return getExchangeRate(currency, exchangeRates).getBuy();
    }

    private ExchangeRate getExchangeRate(String currency, List<ExchangeRate> exchangeRates) {
        return exchangeRates
            .stream()
            .filter(er -> er.getCcy().equals(currency))
            .findFirst().orElse(
                exchangeRateRepository.findLastByCcyInDateRange(currency, getStartOfToday(), getEndOfToday())
                    .orElseThrow(() -> new ExchangeServiceNotFoundException(String.format(CURRENCY_NOT_FOUND, currency))));
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
}
