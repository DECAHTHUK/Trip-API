package ru.tinkoff.lab.tripAPI.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class DestinationCreateException extends ObjectCreateException {
    public DestinationCreateException() {
        super();
    }

    public DestinationCreateException(String message, Throwable cause) {
        super(message, cause);
    }

    public DestinationCreateException(String message) {
        super(message);
    }

    public DestinationCreateException(Throwable cause) {
        super(cause);
    }
}
