package com.airline.exceptions;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ResourceExceptionHandler {

    @ExceptionHandler(BookingNotFoundException.class)
    public ResponseEntity<StandardError> flightNotFound(BookingNotFoundException e, HttpServletRequest request) {

        StandardError error = StandardError.builder()
                .timestamp(ZonedDateTime.now(ZoneId.of("America/Sao_Paulo")).toInstant())
                .status(HttpStatus.NOT_FOUND.value())
                .error("Booking Not Found")
                .message(e.getMessage())
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(UserBookingsNotFoundException.class)
    public ResponseEntity<StandardError> userBookingsNotFound(UserBookingsNotFoundException e,
            HttpServletRequest request) {

        StandardError error = StandardError.builder()
                .timestamp(ZonedDateTime.now(ZoneId.of("America/Sao_Paulo")).toInstant())
                .status(HttpStatus.NOT_FOUND.value())
                .error("Bookings Not Found")
                .message(e.getMessage())
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
}
