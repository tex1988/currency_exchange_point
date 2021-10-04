package com.github.oleksii.zinkevych.currency_exchange_point.model.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorResponse {

    private int statusCode;
    private String ErrorMessage;
}
