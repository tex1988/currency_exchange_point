package com.github.oleksii.zinkevych.currency_exchange_point.service.exception;

public class ApplicationServiceException extends IllegalStateException {
    public ApplicationServiceException() {
    }

    public ApplicationServiceException(String s) {
        super(s);
    }

    public ApplicationServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public ApplicationServiceException(Throwable cause) {
        super(cause);
    }
}
