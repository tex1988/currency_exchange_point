package com.github.oleksii.zinkevych.currency_exchange_point.service.common;

import java.math.BigDecimal;

import com.github.oleksii.zinkevych.currency_exchange_point.constant.ExchangeMode;

public interface ExchangeService {

    BigDecimal calculateExchange(String saleCurrency, String purchaseCurrency,
                                 BigDecimal purchaseAmount, ExchangeMode exchangeMode, String proxyCurrency);
}
