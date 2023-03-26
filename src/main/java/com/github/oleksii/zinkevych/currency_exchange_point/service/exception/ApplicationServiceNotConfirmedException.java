package com.github.oleksii.zinkevych.currency_exchange_point.service.exception;

public class ApplicationServiceNotConfirmedException extends IllegalArgumentException {
    public ApplicationServiceNotConfirmedException() {
    }

    public ApplicationServiceNotConfirmedException(String s) {
        super(s);
    }

    public ApplicationServiceNotConfirmedException(String message, Throwable cause) {
        super(message, cause);
    }

    public ApplicationServiceNotConfirmedException(Throwable cause) {
        super(cause);
    }
}
