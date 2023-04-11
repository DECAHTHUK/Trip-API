package ru.tinkoff.lab.tripAPI.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class TripCreateException extends ObjectCreateException {
    public TripCreateException() {
        super();
    }

    public TripCreateException(String message, Throwable cause) {
        super(message, cause);
    }

    public TripCreateException(String message) {
        super(message);
    }

    public TripCreateException(Throwable cause) {
        super(cause);
    }
}
