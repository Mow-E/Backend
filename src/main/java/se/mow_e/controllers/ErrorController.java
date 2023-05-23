package se.mow_e.controllers;

import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.ConstraintViolationException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Validated
public class ErrorController {

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<?> handleStatusException(ResponseStatusException ex) {

        return ResponseEntity.status(ex.getRawStatusCode()).body(Map.of(
                "status", "error",
                "code", ex.getRawStatusCode(),
                "reason", ex.getReason()
        ));
    }

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<?> handleStatusException(SQLIntegrityConstraintViolationException ex) {

        return ResponseEntity.status(ex.getErrorCode()).body(Map.of(
                "status", "error",
                "code", ex.getErrorCode(),
                "reason", ex.getLocalizedMessage(),
                "sql", ex.getSQLState()
        ));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, Object> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, Object> errors = new HashMap<>();

        errors.put("status", "error");

        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return errors;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public Map<String, Object> handleValidationExceptions(ConstraintViolationException ex) {
        Map<String, Object> errors = new HashMap<>();

        errors.put("status", "error");

        ex.getConstraintViolations().forEach((error) -> {
            PathImpl path = (PathImpl) error.getPropertyPath();
            errors.put(path.getLeafNode().toString(), error.getMessage());
        });

        return errors;
    }
}
