package com.github.oleksii.zinkevych.currency_exchange_point.service.exception;

public class ExchangeServiceNotFoundException extends IllegalStateException {
    public ExchangeServiceNotFoundException() {
    }

    public ExchangeServiceNotFoundException(String s) {
        super(s);
    }

    public ExchangeServiceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExchangeServiceNotFoundException(Throwable cause) {
        super(cause);
    }
}
