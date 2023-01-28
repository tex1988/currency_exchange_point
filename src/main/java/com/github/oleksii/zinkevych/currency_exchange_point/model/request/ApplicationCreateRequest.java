package com.github.oleksii.zinkevych.currency_exchange_point.model.request;

import java.math.BigDecimal;

import com.github.oleksii.zinkevych.currency_exchange_point.constant.ExchangeMode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ApplicationCreateRequest {

    private final static int CURRENCY_NAME_LENGTH = 3;
    private final static int NAME_MAX_LENGTH = 50;

    @NotBlank(message = "The 'saleCurrency' must be specified")
    @Size(min = CURRENCY_NAME_LENGTH, max = CURRENCY_NAME_LENGTH,
        message = "Length of 'saleCurrency' must be equals {max} characters")
    private String saleCurrency;

    @NotBlank(message = "The 'purchaseCurrency' must be specified")
    @Size(min = CURRENCY_NAME_LENGTH, max = CURRENCY_NAME_LENGTH,
        message = "Length of 'purchaseCurrency' must be equals {max} characters")
    private String purchaseCurrency;

    @NotNull(message = "The 'purchaseAmount' must be specified")
    private BigDecimal purchaseAmount;

    @NotBlank(message = "The 'firstName' must be specified")
    @Size(max = NAME_MAX_LENGTH, message = "Length of 'firstName' must be less or equals {max} characters")
    private String firstName;

    @NotBlank(message = "The 'lastName' must be specified")
    @Size(max = NAME_MAX_LENGTH, message = "Length of 'lastName' must be less or equals {max} characters")
    private String lastName;

    @NotBlank(message = "The 'phone' must be specified")
    @Pattern(regexp = "^(\\+)?([- _():=+]?\\d[- _():=+]?){10,13}(\\s*)?$")
    @Size(min = 9, max = 13)
    private String phone;

    @NotNull(message = "The 'exchangeMode' must be specified")
    private ExchangeMode exchangeMode;
}
