package com.github.oleksii.zinkevych.currency_exchange_point.entity;

import java.time.LocalDateTime;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.github.oleksii.zinkevych.currency_exchange_point.entity.common.ApplicationStatus;
import com.github.oleksii.zinkevych.currency_exchange_point.entity.common.ApplicationStatusAttributeConverter;
import lombok.Data;

@Data
@Entity
public class Application {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String saleCurrency;

    private String purchaseCurrency;

    private Double purchaseAmount;

    private Double dealAmount;

    private String firstName;

    private String lastName;

    private String phone;

    @Convert(converter = ApplicationStatusAttributeConverter.class)
    private ApplicationStatus status;

    private Integer otp;

    private LocalDateTime createDate;
}
