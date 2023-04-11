package ru.tinkoff.lab.tripAPI.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class NotificationCreateException extends ObjectCreateException {
    public NotificationCreateException() {
        super();
    }

    public NotificationCreateException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotificationCreateException(String message) {
        super(message);
    }

    public NotificationCreateException(Throwable cause) {
        super(cause);
    }
}
