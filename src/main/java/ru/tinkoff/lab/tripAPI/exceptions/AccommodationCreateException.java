package ru.tinkoff.lab.tripAPI.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class AccommodationCreateException extends ObjectCreateException {
    public AccommodationCreateException() {
        super();
    }

    public AccommodationCreateException(String message, Throwable cause) {
        super(message, cause);
    }

    public AccommodationCreateException(String message) {
        super(message);
    }

    public AccommodationCreateException(Throwable cause) {
        super(cause);
    }
}
