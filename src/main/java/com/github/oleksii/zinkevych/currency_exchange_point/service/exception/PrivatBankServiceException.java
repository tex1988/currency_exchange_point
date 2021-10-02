package com.github.oleksii.zinkevych.currency_exchange_point.service.exception;

public class PrivatBankServiceException extends BankServiceException {
    public PrivatBankServiceException() {
    }

    public PrivatBankServiceException(String s) {
        super(s);
    }

    public PrivatBankServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public PrivatBankServiceException(Throwable cause) {
        super(cause);
    }
}
