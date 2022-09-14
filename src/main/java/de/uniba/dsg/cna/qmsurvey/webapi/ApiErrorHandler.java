package de.uniba.dsg.cna.qmsurvey.webapi;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice(annotations = RestController.class)
public class ApiErrorHandler {

    @ExceptionHandler(ResponseStatusException.class)
    protected ResponseEntity<Object> handleException(ResponseStatusException exception) {
        ErrorResponse errorResponse = new ErrorResponse(
                exception.getStatus().value(),
                exception.getReason());

        return ResponseEntity.status(exception.getStatus()).body(errorResponse);
    }

    @ExceptionHandler(RuntimeException.class)
    protected ResponseEntity<Object> handleGenericException(RuntimeException exception) {
        ErrorResponse errorResponse = new ErrorResponse(
                500,
                "Internal Error. Please try again later.");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        ErrorResponse errorResponse = new ErrorResponse(
                400,
                "Invalid request",
                errors
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
}
