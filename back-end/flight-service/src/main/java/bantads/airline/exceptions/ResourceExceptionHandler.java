package bantads.airline.exceptions;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ResourceExceptionHandler {

    @ExceptionHandler(FlightNotFoundException.class)
    public ResponseEntity<StandardError> flightNotFound(FlightNotFoundException e, HttpServletRequest request) {

        StandardError error = StandardError.builder()
                .timestamp(ZonedDateTime.now(ZoneId.of("America/Sao_Paulo")).toInstant())
                .status(HttpStatus.NOT_FOUND.value())
                .error("Voo Nao encontrado")
                .message(e.getMessage())
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(NoFlightNotFoundException.class)
    public ResponseEntity<StandardError> noFlightFound(NoFlightNotFoundException e, HttpServletRequest request) {

        StandardError error = StandardError.builder()
                .timestamp(ZonedDateTime.now(ZoneId.of("America/Sao_Paulo")).toInstant())
                .status(HttpStatus.NOT_FOUND.value())
                .error("No Flights Found")
                .message(e.getMessage())
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(NewFlightException.class)
    public ResponseEntity<StandardError> flightNotCreated(NewFlightException e, HttpServletRequest request) {

        StandardError error = StandardError.builder()
                .timestamp(ZonedDateTime.now(ZoneId.of("America/Sao_Paulo")).toInstant())
                .status(HttpStatus.NOT_FOUND.value())
                .error("No Flights Found")
                .message(e.getMessage())
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
}
