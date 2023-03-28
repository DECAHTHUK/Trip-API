package ru.tinkoff.lab.tripAPI.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UserCreateException extends ObjectCreateException {
    public UserCreateException() {
        super();
    }

    public UserCreateException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserCreateException(String message) {
        super(message);
    }

    public UserCreateException(Throwable cause) {
        super(cause);
    }

    public HttpStatus getCode() {
        return HttpStatus.BAD_REQUEST;
    }
}
