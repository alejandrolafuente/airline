package com.airline.exceptions;

public class UserBookingsNotFoundException extends RuntimeException {

    public UserBookingsNotFoundException(String msg) {
        super(msg);
    }
}
