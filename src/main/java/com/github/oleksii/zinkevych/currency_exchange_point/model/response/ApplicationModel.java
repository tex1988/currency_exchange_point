package com.github.oleksii.zinkevych.currency_exchange_point.model.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.github.oleksii.zinkevych.currency_exchange_point.constant.ApplicationStatus;
import com.github.oleksii.zinkevych.currency_exchange_point.constant.ExchangeMode;
import lombok.Data;

@Data
public class ApplicationModel {

    private Long id;
    private String purchaseCurrency;
    private String saleCurrency;
    private ExchangeMode exchangeMode;
    private String proxyCurrency;
    private BigDecimal purchaseAmount;
    private BigDecimal dealAmount;
    private String firstName;
    private String lastName;
    private String phone;
    private ApplicationStatus status;
    private LocalDateTime createDate;
    private LocalDateTime confirmDate;
    private Integer otp;

}
