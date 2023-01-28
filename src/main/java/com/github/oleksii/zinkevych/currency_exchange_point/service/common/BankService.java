package com.github.oleksii.zinkevych.currency_exchange_point.service.common;

import java.util.List;

import com.github.oleksii.zinkevych.currency_exchange_point.entity.ExchangeRate;

public interface BankService {

    List<ExchangeRate> getExchangeRates() throws IllegalStateException;
}
