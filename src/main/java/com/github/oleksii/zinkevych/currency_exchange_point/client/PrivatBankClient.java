package com.github.oleksii.zinkevych.currency_exchange_point.client;

import java.util.List;

import com.github.oleksii.zinkevych.currency_exchange_point.entity.ExchangeRate;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;

public interface PrivatBankClient {
    @GetExchange("/pubinfo")
    List<ExchangeRate> getExchangeRates(@RequestParam Integer coursid);
}
