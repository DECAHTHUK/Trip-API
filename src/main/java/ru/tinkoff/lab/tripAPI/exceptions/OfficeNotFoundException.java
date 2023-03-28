package ru.tinkoff.lab.tripAPI.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class OfficeNotFoundException extends ObjectNotFoundException {
    public OfficeNotFoundException() {
        super();
    }

    public OfficeNotFoundException(String message) {
        super(message);
    }

    public OfficeNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public OfficeNotFoundException(Throwable cause) {
        super(cause);
    }
}
