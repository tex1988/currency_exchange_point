package com.github.oleksii.zinkevych.currency_exchange_point.model.response;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class ApplicationResponse {

    private Long id;
    private String purchaseCurrency;
    private String saleCurrency;
    private BigDecimal purchaseAmount;
    private BigDecimal dealAmount;
    private String phone;
    private Integer otp;
}
