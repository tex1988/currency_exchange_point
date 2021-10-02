package com.github.oleksii.zinkevych.currency_exchange_point.model.response;

import lombok.Data;

@Data
public class ApplicationResponse {
    String purchaseCurrency;
    String saleCurrency;
    Double dealAmount;
    Long phone;
    Integer otp;
}
