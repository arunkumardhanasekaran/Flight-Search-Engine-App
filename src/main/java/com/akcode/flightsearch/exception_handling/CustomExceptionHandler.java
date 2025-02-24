package com.akcode.flightsearch.exception_handling;

import com.akcode.flightsearch.model.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(NoFlightsAvailableException.class)
    public ResponseEntity<ErrorResponse> handleNoFlightsAvailableException(NoFlightsAvailableException e) {
        return new ResponseEntity<>(ErrorResponse.builder()
                .errorCode(1001)
                .errorMessage(e.getMessage())
                .build(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(LocationNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleLocationNotFoundException(LocationNotFoundException e) {
        return new ResponseEntity<>(ErrorResponse.builder()
                .errorCode(1002)
                .errorMessage(e.getMessage())
                .build(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(GenericException.class)
    public ResponseEntity<ErrorResponse> handleGenericException(GenericException e) {
        return new ResponseEntity<>(ErrorResponse.builder()
                .errorCode(1003)
                .errorMessage(e.getMessage() == null ? "Generic Error" : e.getMessage())
                .build(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
