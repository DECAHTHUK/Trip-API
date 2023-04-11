package ru.tinkoff.lab.tripAPI.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class DestinationNotFoundException extends ObjectNotFoundException {
    public DestinationNotFoundException() {
        super();
    }

    public DestinationNotFoundException(String message) {
        super(message);
    }

    public DestinationNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public DestinationNotFoundException(Throwable cause) {
        super(cause);
    }
}
