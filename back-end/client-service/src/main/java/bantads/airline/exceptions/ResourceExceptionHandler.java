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

    @ExceptionHandler(ClientNotFoundException.class)
    public ResponseEntity<StandardError> clientNotFound(ClientNotFoundException e, HttpServletRequest request) {

        StandardError error = StandardError.builder()
                .timestamp(ZonedDateTime.now(ZoneId.of("America/Sao_Paulo")).toInstant())
                .status(HttpStatus.NOT_FOUND.value())
                .error("Client Not Found")
                .message(e.getMessage())
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(MilesTransactionException.class)
    public ResponseEntity<StandardError> transactionNotCompleted(MilesTransactionException e,
            HttpServletRequest request) {

        StandardError error = StandardError.builder()
                .timestamp(ZonedDateTime.now(ZoneId.of("America/Sao_Paulo")).toInstant())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error("Transaction not completed")
                .message(e.getMessage())
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    @ExceptionHandler(ClientUpdateException.class)
    public ResponseEntity<StandardError> balanceNotUpdated(ClientUpdateException e,
            HttpServletRequest request) {

        StandardError error = StandardError.builder()
                .timestamp(ZonedDateTime.now(ZoneId.of("America/Sao_Paulo")).toInstant())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error("Balance not updated")
                .message(e.getMessage())
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
