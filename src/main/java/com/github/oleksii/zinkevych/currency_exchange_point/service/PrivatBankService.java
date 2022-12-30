package com.github.oleksii.zinkevych.currency_exchange_point.service;

import java.time.LocalDate;
import java.util.List;

import com.github.oleksii.zinkevych.currency_exchange_point.client.PrivatBankClient;
import com.github.oleksii.zinkevych.currency_exchange_point.entity.ExchangeRate;
import com.github.oleksii.zinkevych.currency_exchange_point.repository.ExchangeRateRepository;
import com.github.oleksii.zinkevych.currency_exchange_point.service.common.BankService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class PrivatBankService implements BankService {

    private final static Integer COURSE_ID = 5;

    private final ExchangeRateRepository repository;
    private final PrivatBankClient pbClient;

    @Override
    public void getExchangeRates() throws IllegalStateException {
        List<ExchangeRate> exchangeRates = pbClient.getExchangeRates(COURSE_ID);
        LocalDate date = LocalDate.now();
        exchangeRates.forEach(exchangeRate -> exchangeRate.setDate(date));
        repository.saveAll(exchangeRates);
    }
}
