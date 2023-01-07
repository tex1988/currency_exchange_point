package com.github.oleksii.zinkevych.currency_exchange_point.model.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ApplicationConfirmRequest {

    private static final int MIN_OTP_VALUE = 100000;
    private static final int MAX_OTP_VALUE = 999999;

    @NotNull(message = "'id' must be specified")
    @Min(value = Long.MIN_VALUE, message = "'id' value must be not less than {value}")
    @Max(value = Long.MAX_VALUE, message = "'id' value must be not greater than {value}")
    private Long id;

    @NotNull(message = "'otp' must be specified")
    @Min(value = MIN_OTP_VALUE, message = "'otp' value must be not less than {value}")
    @Max(value = MAX_OTP_VALUE, message = "'otp' value must be not greater than {value}")
    private Integer otp;
}
