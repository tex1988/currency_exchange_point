package com.github.oleksii.zinkevych.currency_exchange_point.service.exception;

public class NotFoundExchangeServiceException extends ExchangeServiceException {
    public NotFoundExchangeServiceException() {
    }

    public NotFoundExchangeServiceException(String s) {
        super(s);
    }

    public NotFoundExchangeServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotFoundExchangeServiceException(Throwable cause) {
        super(cause);
    }
}
