package ru.tinkoff.lab.tripAPI.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class OfficeCreateException extends ObjectCreateException {
    public OfficeCreateException() {
        super();
    }

    public OfficeCreateException(String message, Throwable cause) {
        super(message, cause);
    }

    public OfficeCreateException(String message) {
        super(message);
    }

    public OfficeCreateException(Throwable cause) {
        super(cause);
    }
}
