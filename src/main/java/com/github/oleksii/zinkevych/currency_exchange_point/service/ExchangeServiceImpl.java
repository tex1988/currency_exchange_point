package com.github.oleksii.zinkevych.currency_exchange_point.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import com.github.oleksii.zinkevych.currency_exchange_point.repository.ExchangeRateRepository;
import com.github.oleksii.zinkevych.currency_exchange_point.service.common.ExchangeService;
import com.github.oleksii.zinkevych.currency_exchange_point.service.exception.NotFoundExchangeServiceException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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

    @Override
    public BigDecimal calculateExchange(String saleCurrency, String purchaseCurrency, BigDecimal purchaseAmount) {
        BigDecimal result;
        LocalDateTime from = LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT);
        LocalDateTime to = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
        if (purchaseCurrency.equals(BTC) || saleCurrency.equals(BTC)) {
            result = calculateForBtc(saleCurrency, purchaseCurrency, purchaseAmount, from, to);
        } else {
            result = calculateForCurrency(saleCurrency, purchaseCurrency, purchaseAmount, from, to);
        }
        return result;
    }

    private BigDecimal calculateForCurrency(String saleCurrency, String purchaseCurrency, BigDecimal purchaseAmount,
                                            LocalDateTime from, LocalDateTime to) {
        BigDecimal result;
        if (purchaseCurrency.equals(UAH)) {
            result = getCurrencyAmountForUahSale(saleCurrency, purchaseAmount, from, to).setScale(2, HALF_UP_ROUNDING_MODE);
        } else if (saleCurrency.equals(UAH)) {
            result = getUahAmountForCurrencySale(purchaseCurrency, purchaseAmount, from, to).setScale(2, HALF_UP_ROUNDING_MODE);
        } else {
            BigDecimal purchaseCurrencyInUah = getUahAmountForCurrencySale(purchaseCurrency, purchaseAmount, from, to);
            BigDecimal saleCurrencySaleRate = getCurrencySaleRate(saleCurrency, from, to);
            result = purchaseCurrencyInUah.divide(saleCurrencySaleRate, 2, HALF_UP_ROUNDING_MODE);
        }
        return result;
    }

    private BigDecimal calculateForBtc(String saleCurrency, String purchaseCurrency, BigDecimal purchaseAmount,
                                       LocalDateTime from, LocalDateTime to) {
        BigDecimal result;
        if (saleCurrency.equals(BTC) && purchaseCurrency.equals(USD)) {
            result = getUsdAmountForBtcSale(purchaseAmount, from, to).setScale(2, HALF_UP_ROUNDING_MODE);
        } else if (saleCurrency.equals(USD) && purchaseCurrency.equals(BTC)) {
            result = getBtcAmountFromUsdSale(purchaseAmount, from, to).setScale(2, HALF_UP_ROUNDING_MODE);
        } else if (saleCurrency.equals(BTC)) {
            BigDecimal buyBtcRate = getCurrencyBuyRate(BTC, from, to);
            BigDecimal usdAmount = calculateForCurrency(USD, purchaseCurrency, purchaseAmount, from, to);
            result = usdAmount.divide(buyBtcRate, 5, RoundingMode.HALF_UP).setScale(2, HALF_UP_ROUNDING_MODE);
        } else {
            BigDecimal saleBtcRate = getCurrencySaleRate(BTC, from, to);
            BigDecimal usdAmount = purchaseAmount.multiply(saleBtcRate);
            result = calculateForCurrency(saleCurrency, USD, usdAmount, from, to).setScale(2, HALF_UP_ROUNDING_MODE);
        }
        return result;
    }

    private BigDecimal getUahAmountForCurrencySale(String currency, BigDecimal currencyAmount,
                                                   LocalDateTime from, LocalDateTime to) {
        BigDecimal purchaseCurrencySaleRate = getCurrencySaleRate(currency, from, to);
        return purchaseCurrencySaleRate.multiply(currencyAmount);
    }

    private BigDecimal getCurrencyAmountForUahSale(String currency, BigDecimal uahAmount,
                                                   LocalDateTime from, LocalDateTime to) {
        BigDecimal purchaseCurrencyBuyRate = getCurrencyBuyRate(currency, from, to);
        return purchaseCurrencyBuyRate.divide(uahAmount, 5, HALF_UP_ROUNDING_MODE);
    }

    private BigDecimal getUsdAmountForBtcSale(BigDecimal purchaseAmount, LocalDateTime from, LocalDateTime to) {
        BigDecimal buyBtcRate = getCurrencyBuyRate(BTC, from, to);
        return purchaseAmount.divide(buyBtcRate, 5, HALF_UP_ROUNDING_MODE);
    }

    private BigDecimal getBtcAmountFromUsdSale(BigDecimal purchaseAmount, LocalDateTime from, LocalDateTime to) {
        BigDecimal saleBtcRate = getCurrencySaleRate(BTC, from, to);
        return purchaseAmount.multiply(saleBtcRate);
    }

    private BigDecimal getCurrencySaleRate(String currency, LocalDateTime from, LocalDateTime to) {
        return exchangeRateRepository.findLastByCcyInDateRange(currency, from, to)
            .orElseThrow(() -> new NotFoundExchangeServiceException(String.format(CURRENCY_NOT_FOUND, currency)))
            .getSale();
    }

    private BigDecimal getCurrencyBuyRate(String currency, LocalDateTime from, LocalDateTime to) {
        return exchangeRateRepository.findLastByCcyInDateRange(currency, from, to)
            .orElseThrow(() -> new NotFoundExchangeServiceException(String.format(CURRENCY_NOT_FOUND, currency)))
            .getBuy();
    }
}
