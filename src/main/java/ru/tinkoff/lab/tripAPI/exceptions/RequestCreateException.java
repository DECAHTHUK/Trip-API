package ru.tinkoff.lab.tripAPI.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class RequestCreateException extends ObjectCreateException {
    public RequestCreateException() {
        super();
    }

    public RequestCreateException(String message, Throwable cause) {
        super(message, cause);
    }

    public RequestCreateException(String message) {
        super(message);
    }

    public RequestCreateException(Throwable cause) {
        super(cause);
    }
}
