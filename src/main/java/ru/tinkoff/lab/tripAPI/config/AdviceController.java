package ru.tinkoff.lab.tripAPI.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.tinkoff.lab.tripAPI.exceptions.ObjectCreateException;
import ru.tinkoff.lab.tripAPI.exceptions.ObjectNotFoundException;
import ru.tinkoff.lab.tripAPI.exceptions.TripDtoNullException;

@ControllerAdvice
public class AdviceController {
    @ExceptionHandler(value = {ObjectNotFoundException.class})
    public ResponseEntity<Object> handleNotFound(ObjectNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {ObjectCreateException.class})
    public ResponseEntity<Object> handleCreate(ObjectCreateException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {TripDtoNullException.class})
    public ResponseEntity<Object> handleNullTripDto(TripDtoNullException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
