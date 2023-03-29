package ru.tinkoff.lab.tripAPI.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ObjectCreateException extends RuntimeException{
    public ObjectCreateException() {
        super();
    }

    public ObjectCreateException(String message, Throwable cause) {
        super(message, cause);
    }

    public ObjectCreateException(String message) {
        super(message);
    }

    public ObjectCreateException(Throwable cause) {
        super(cause);
    }

    public HttpStatus getStatusCode() {
        return HttpStatus.BAD_REQUEST;
    }
}
