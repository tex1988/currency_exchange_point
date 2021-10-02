package com.github.oleksii.zinkevych.currency_exchange_point.service.common;

public interface ExchangeService {
    double calculateExchange(String saleCurrency, String purchaseCurrency, double purchaseAmount);
}
