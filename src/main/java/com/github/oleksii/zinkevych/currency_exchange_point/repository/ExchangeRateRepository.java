package com.github.oleksii.zinkevych.currency_exchange_point.repository;

import java.time.LocalDate;
import java.util.Optional;

import com.github.oleksii.zinkevych.currency_exchange_point.entity.ExchangeRate;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExchangeRateRepository extends CrudRepository<ExchangeRate, Long> {
    String FIND_BY_CCY_AND_DATE_QUERY = "SELECT er.id,\n" +
                                   "       er.buy,\n" +
                                   "       er.base_ccy,\n" +
                                   "       er.ccy,\n" +
                                   "       er.date,\n" +
                                   "       er.sale\n" +
                                   "FROM   exchange_rate er\n" +
                                   "WHERE  er.ccy = ?\n" +
                                   "   AND er.date = ? ";
    @Query(value = FIND_BY_CCY_AND_DATE_QUERY, nativeQuery = true)
    Optional<ExchangeRate> findByCcyAndDate(String ccy, LocalDate date);
}
