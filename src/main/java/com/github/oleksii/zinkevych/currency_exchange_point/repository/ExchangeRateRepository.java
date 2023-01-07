package com.github.oleksii.zinkevych.currency_exchange_point.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.github.oleksii.zinkevych.currency_exchange_point.entity.ExchangeRate;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExchangeRateRepository extends CrudRepository<ExchangeRate, Long> {
    String FIND_BY_CCY_AND_DATE_QUERY = """
         SELECT er.id,
                er.buy,
                er.base_ccy,
                er.ccy,
                er.date,
                er.sale
        FROM exchange_rate er
        WHERE er.ccy = ?
        AND er.date BETWEEN ? AND ?
        ORDER BY er.id DESC LIMIT 1
        """;
    @Query(value = FIND_BY_CCY_AND_DATE_QUERY, nativeQuery = true)
    Optional<ExchangeRate> findLastByCcyInDateRange(String ccy, LocalDateTime from, LocalDateTime to);

    List<ExchangeRate> findAllByCcyAndDateBetween(String ccy, LocalDateTime from, LocalDateTime to);
}
