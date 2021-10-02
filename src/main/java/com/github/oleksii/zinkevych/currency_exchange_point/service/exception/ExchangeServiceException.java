package com.github.oleksii.zinkevych.currency_exchange_point.service.exception;

public class ExchangeServiceException extends IllegalStateException {
    public ExchangeServiceException() {
    }

    public ExchangeServiceException(String s) {
        super(s);
    }

    public ExchangeServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExchangeServiceException(Throwable cause) {
        super(cause);
    }
}
