package com.github.oleksii.zinkevych.currency_exchange_point.service.common;

import java.math.BigDecimal;

public interface ExchangeService {

    BigDecimal calculateExchange(String saleCurrency, String purchaseCurrency, BigDecimal purchaseAmount);
}
