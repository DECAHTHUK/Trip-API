package ru.tinkoff.lab.tripAPI.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class AccommodationNotFoundException extends ObjectNotFoundException {
    public AccommodationNotFoundException() {
        super();
    }

    public AccommodationNotFoundException(String message) {
        super(message);
    }

    public AccommodationNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public AccommodationNotFoundException(Throwable cause) {
        super(cause);
    }
}
