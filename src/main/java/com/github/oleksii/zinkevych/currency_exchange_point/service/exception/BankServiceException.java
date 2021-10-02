package com.github.oleksii.zinkevych.currency_exchange_point.service.exception;

public class BankServiceException extends IllegalStateException {
    public BankServiceException() {
    }

    public BankServiceException(String s) {
        super(s);
    }

    public BankServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public BankServiceException(Throwable cause) {
        super(cause);
    }
}
