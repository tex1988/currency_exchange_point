package com.github.oleksii.zinkevych.currency_exchange_point.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

import com.github.oleksii.zinkevych.currency_exchange_point.repository.ExchangeRateRepository;
import com.github.oleksii.zinkevych.currency_exchange_point.service.common.ExchangeService;
import com.github.oleksii.zinkevych.currency_exchange_point.service.exception.NotFoundExchangeServiceException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class ExchangeServiceImpl implements ExchangeService {

    private static final String CURRENCY_NOT_FOUND = "Currency '%s' not found";

    private static final RoundingMode HALF_UP_ROUNDING_MODE = RoundingMode.HALF_UP;

    private static LocalDate localDate = LocalDate.now();

    private final ExchangeRateRepository exchangeRateRepository;

    @Override
    public BigDecimal calculateExchange(String saleCurrency, String purchaseCurrency, BigDecimal purchaseAmount) {
        BigDecimal result;
        if (purchaseCurrency.equals("BTC") || saleCurrency.equals("BTC")) {
            result = calculateForBtc(saleCurrency, purchaseCurrency, purchaseAmount);
        } else {
            result = calculateForCurrency(saleCurrency, purchaseCurrency, purchaseAmount);
        }
        return result;
    }

    private BigDecimal calculateForCurrency(String saleCurrency, String purchaseCurrency, BigDecimal purchaseAmount) {
        BigDecimal result;
        if (purchaseCurrency.equals("UAH")) {
            result = getCurrencyAmountForUahSale(saleCurrency, purchaseAmount).setScale(2, HALF_UP_ROUNDING_MODE);
        } else if (saleCurrency.equals("UAH")) {
            result = getUahAmountForCurrencySale(purchaseCurrency, purchaseAmount).setScale(2, HALF_UP_ROUNDING_MODE);
        } else {
            BigDecimal purchaseCurrencyInUah = getUahAmountForCurrencySale(purchaseCurrency, purchaseAmount);
            BigDecimal saleCurrencySaleRate = getCurrencySaleRate(saleCurrency);
            result = purchaseCurrencyInUah.divide(saleCurrencySaleRate, 2, HALF_UP_ROUNDING_MODE);
        }
        return result;
    }

    private BigDecimal calculateForBtc(String saleCurrency, String purchaseCurrency, BigDecimal purchaseAmount) {
        BigDecimal result;
        if (saleCurrency.equals("BTC") && purchaseCurrency.equals("USD")) {
            result = getUsdAmountForBtcSale(purchaseAmount).setScale(2, HALF_UP_ROUNDING_MODE);
        } else if (saleCurrency.equals("USD") && purchaseCurrency.equals("BTC")) {
            result = getBtcAmountFromUsdSale(purchaseAmount).setScale(2, HALF_UP_ROUNDING_MODE);
        } else if (saleCurrency.equals("BTC")) {
            BigDecimal buyBtcRate = getCurrencyBuyRate("BTC");
            BigDecimal usdAmount = calculateForCurrency("USD", purchaseCurrency, purchaseAmount);
            result = usdAmount.divide(buyBtcRate, 5, RoundingMode.HALF_UP).setScale(2, HALF_UP_ROUNDING_MODE);
        } else {
            BigDecimal saleBtcRate = getCurrencySaleRate("BTC");
            BigDecimal usdAmount = purchaseAmount.multiply(saleBtcRate);
            result = calculateForCurrency(saleCurrency, "USD", usdAmount).setScale(2, HALF_UP_ROUNDING_MODE);
        }
        return result;
    }

    private BigDecimal getUahAmountForCurrencySale(String currency, BigDecimal currencyAmount) {
        BigDecimal purchaseCurrencySaleRate = getCurrencySaleRate(currency);
        return purchaseCurrencySaleRate.multiply(currencyAmount);
    }

    private BigDecimal getCurrencyAmountForUahSale(String currency, BigDecimal uahAmount) {
        BigDecimal purchaseCurrencyBuyRate = getCurrencyBuyRate(currency);
        return purchaseCurrencyBuyRate.divide(uahAmount, 5, HALF_UP_ROUNDING_MODE);
    }

    private BigDecimal getUsdAmountForBtcSale(BigDecimal purchaseAmount) {
        BigDecimal buyBtcRate = getCurrencyBuyRate("BTC");
        return purchaseAmount.divide(buyBtcRate, 5, HALF_UP_ROUNDING_MODE);
    }

    private BigDecimal getBtcAmountFromUsdSale(BigDecimal purchaseAmount) {
        BigDecimal saleBtcRate = getCurrencySaleRate("BTC");
        return purchaseAmount.multiply(saleBtcRate);
    }

    private BigDecimal getCurrencySaleRate(String currency) {
        return exchangeRateRepository.findByCcyAndDate(currency, localDate)
            .orElseThrow(() -> new NotFoundExchangeServiceException(String.format(CURRENCY_NOT_FOUND, currency)))
            .getSale();
    }

    private BigDecimal getCurrencyBuyRate(String currency) {
        return exchangeRateRepository.findByCcyAndDate(currency, localDate)
            .orElseThrow(() -> new NotFoundExchangeServiceException(String.format(CURRENCY_NOT_FOUND, currency)))
            .getBuy();
    }

    @Scheduled(cron = "0 0 0 * * *")
    private void getCurrentDate() {
        localDate = LocalDate.now();
    }

    private void nothing(){}
}
