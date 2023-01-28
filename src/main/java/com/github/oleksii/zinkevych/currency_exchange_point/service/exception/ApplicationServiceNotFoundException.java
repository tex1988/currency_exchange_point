package com.github.oleksii.zinkevych.currency_exchange_point.service.exception;

public class ApplicationServiceNotFoundException extends IllegalStateException {
    public ApplicationServiceNotFoundException() {
    }

    public ApplicationServiceNotFoundException(String s) {
        super(s);
    }

    public ApplicationServiceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ApplicationServiceNotFoundException(Throwable cause) {
        super(cause);
    }
}
