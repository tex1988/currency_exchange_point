package com.github.oleksii.zinkevych.currency_exchange_point.service.exception;

public class NotFoundApplicationServiceException extends ApplicationServiceException {
    public NotFoundApplicationServiceException() {
    }

    public NotFoundApplicationServiceException(String s) {
        super(s);
    }

    public NotFoundApplicationServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotFoundApplicationServiceException(Throwable cause) {
        super(cause);
    }
}
