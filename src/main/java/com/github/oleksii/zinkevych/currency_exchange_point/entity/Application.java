package com.github.oleksii.zinkevych.currency_exchange_point.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Currency;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.github.oleksii.zinkevych.currency_exchange_point.constant.ApplicationStatus;
import com.github.oleksii.zinkevych.currency_exchange_point.converter.ApplicationStatusAttributeConverter;
import lombok.Data;

@Data
@Entity
public class Application {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private Currency saleCurrency;

    private Currency purchaseCurrency;

    private BigDecimal purchaseAmount;

    private BigDecimal dealAmount;

    private String firstName;

    private String lastName;

    private String phone;

    @Convert(converter = ApplicationStatusAttributeConverter.class)
    private ApplicationStatus status;

    private Integer otp;

    private LocalDateTime createDate;
}
