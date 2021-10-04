package com.github.oleksii.zinkevych.currency_exchange_point.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class ExchangeRate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String ccy;

    private String base_ccy;

    private BigDecimal buy;

    private BigDecimal sale;

    private LocalDate date;
}
