package ru.tinkoff.lab.tripAPI.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class TripDtoNullException extends NullPointerException {
    public TripDtoNullException() {
        super();
    }

    public TripDtoNullException(String message) {
        super(message);
    }
}
