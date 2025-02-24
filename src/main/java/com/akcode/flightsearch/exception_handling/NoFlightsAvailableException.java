package com.akcode.flightsearch.exception_handling;

public class NoFlightsAvailableException extends RuntimeException {

    public NoFlightsAvailableException(String message) {
        super(message);
    }
}
