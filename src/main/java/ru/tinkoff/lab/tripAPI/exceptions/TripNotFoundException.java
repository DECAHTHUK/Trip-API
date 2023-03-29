package ru.tinkoff.lab.tripAPI.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class TripNotFoundException extends ObjectNotFoundException {
    public TripNotFoundException() {
        super();
    }

    public TripNotFoundException(String message) {
        super(message);
    }

    public TripNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public TripNotFoundException(Throwable cause) {
        super(cause);
    }
}
