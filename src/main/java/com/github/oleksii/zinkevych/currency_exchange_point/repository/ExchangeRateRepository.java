package com.github.oleksii.zinkevych.currency_exchange_point.repository;

import java.time.LocalDate;
import java.util.Optional;

import com.github.oleksii.zinkevych.currency_exchange_point.entity.ExchangeRate;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExchangeRateRepository extends CrudRepository<ExchangeRate, Long> {
    Optional<ExchangeRate> findByCcyAndDate(String ccy, LocalDate date);
}
