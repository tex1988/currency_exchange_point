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
    private final static String CURRENCY_NOT_FOUND = "Currency '%s' not found";
    private final ExchangeRateRepository exchangeRateRepository;
    private static LocalDate localDate = LocalDate.now();

    @Override
    public double calculateExchange(String saleCurrency, String purchaseCurrency, double purchaseAmount) {
        double result;
        if (purchaseCurrency.equals("BTC") || saleCurrency.equals("BTC")) {
            result = calculateForBtc(saleCurrency, purchaseCurrency, purchaseAmount);
        } else {
            result = calculateForCurrency(saleCurrency, purchaseCurrency, purchaseAmount);
        }
        return result;
    }

    private double calculateForCurrency(String saleCurrency, String purchaseCurrency, double purchaseAmount) {
        double result;
        if (purchaseCurrency.equals("UAH")) {
            result = round(getCurrencyAmountForUahSale(saleCurrency, purchaseAmount), 2);
        } else if (saleCurrency.equals("UAH")) {
            result = round(getUahAmountForCurrencySale(purchaseCurrency, purchaseAmount), 2);
        } else {
            double purchaseCurrencyInUah = getUahAmountForCurrencySale(purchaseCurrency, purchaseAmount);
            double saleCurrencySaleRate = getCurrencySaleRate(saleCurrency);
            result = round(purchaseCurrencyInUah / saleCurrencySaleRate, 2);
        }
        return result;
    }

    private double calculateForBtc(String saleCurrency, String purchaseCurrency, double purchaseAmount) {
        double result;
        if (saleCurrency.equals("BTC") && purchaseCurrency.equals("USD")) {
            result = round(getUsdAmountForBtcSale(purchaseAmount), 2);
        } else if (saleCurrency.equals("USD") && purchaseCurrency.equals("BTC")) {
            result = round(getBtcAmountFromUsdSale(purchaseAmount), 2);
        } else if (saleCurrency.equals("BTC")) {
            double buyBtcRate = getCurrencyBuyRate("BTC");
            double usdAmount = calculateForCurrency("USD", purchaseCurrency, purchaseAmount);
            result = round(usdAmount / buyBtcRate, 2);
        } else {
            double saleBtcRate = getCurrencySaleRate("BTC");
            double usdAmount = purchaseAmount * saleBtcRate;
            result = calculateForCurrency(saleCurrency, "USD", usdAmount);
        }
        return result;
    }

    private double getUahAmountForCurrencySale(String currency, double currencyAmount) {
        double purchaseCurrencySaleRate = getCurrencySaleRate(currency);
        return purchaseCurrencySaleRate * currencyAmount;
    }

    private double getCurrencyAmountForUahSale(String currency, double uahAmount) {
        double purchaseCurrencyBuyRate = getCurrencyBuyRate(currency);
        return purchaseCurrencyBuyRate / uahAmount;
    }

    private double getUsdAmountForBtcSale(double purchaseAmount) {
        double buyBtcRate = getCurrencyBuyRate("BTC");
        return purchaseAmount / buyBtcRate;
    }

    private double getBtcAmountFromUsdSale(double purchaseAmount) {
        double saleBtcRate = getCurrencySaleRate("BTC");
        return purchaseAmount * saleBtcRate;
    }

    private double getCurrencySaleRate(String currency) {
        return exchangeRateRepository.findByCcyAndDate(currency, localDate)
                .orElseThrow(() -> new NotFoundExchangeServiceException(String.format(CURRENCY_NOT_FOUND, currency)))
                .getSale();
    }

    private double getCurrencyBuyRate(String currency) {
        return exchangeRateRepository.findByCcyAndDate(currency, localDate)
                .orElseThrow(() -> new NotFoundExchangeServiceException(String.format(CURRENCY_NOT_FOUND, currency)))
                .getBuy();
    }

    private double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();
        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    @Scheduled(cron = "0 0 0 * * *")
    private void getCurrentDate() {
        localDate = LocalDate.now();
    }
}
