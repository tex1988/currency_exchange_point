package com.github.oleksii.zinkevych.currency_exchange_point.model.response;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class ApplicationResponse {

    private String purchaseCurrency;
    private String saleCurrency;
    private BigDecimal dealAmount;
    private Long phone;
    private Integer otp;
}
