package com.petclinic.api.exception;

import com.petclinic.api.dto.ErrorResponse;
import com.petclinic.exception.InvalidBirthDateException;
import com.petclinic.exception.InvalidPetTypeException;
import com.petclinic.exception.OwnerNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Global exception handler for REST controllers.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // Extract repeated strings to constants
    private static final String VALIDATION_ERROR_MESSAGE = "Validation failed for pet registration";
    private static final String UNEXPECTED_ERROR_MESSAGE = "An unexpected error occurred. Please try again later.";
    private static final String LOG_VALIDATION_ERROR = "Validation error occurred: {}";
    private static final String LOG_INVALID_PET_TYPE = "Invalid pet type: {}";
    private static final String LOG_OWNER_NOT_FOUND = "Owner not found: {}";
    private static final String LOG_INVALID_BIRTH_DATE = "Invalid birth date: {}";
    private static final String LOG_UNEXPECTED_ERROR = "Unexpected error occurred";

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {

        logger.warn(LOG_VALIDATION_ERROR, ex.getMessage());

        List<ErrorResponse.ValidationError> validationErrors = ex.getBindingResult()
                .getAllErrors()
                .stream()
                .map(error -> {
                    String fieldName = error instanceof FieldError fieldError ? fieldError.getField() : error.getObjectName();
                    Object rejectedValue = error instanceof FieldError fieldError ? fieldError.getRejectedValue() : null;
                    String message = error.getDefaultMessage();
                    return new ErrorResponse.ValidationError(fieldName, rejectedValue, message);
                })
                .toList();

        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                VALIDATION_ERROR_MESSAGE,
                request.getRequestURI(),
                validationErrors
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(InvalidPetTypeException.class)
    public ResponseEntity<ErrorResponse> handleInvalidPetTypeException(
            InvalidPetTypeException ex,
            HttpServletRequest request) {

        logger.warn(LOG_INVALID_PET_TYPE, ex.getPetType());

        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                ex.getMessage(),
                request.getRequestURI(),
                null
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(InvalidBirthDateException.class)
    public ResponseEntity<ErrorResponse> handleInvalidBirthDateException(
            InvalidBirthDateException ex,
            HttpServletRequest request) {

        logger.warn(LOG_INVALID_BIRTH_DATE, ex.getBirthDate());

        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                ex.getMessage(),
                request.getRequestURI(),
                null
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(OwnerNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleOwnerNotFoundException(
            OwnerNotFoundException ex,
            HttpServletRequest request) {

        logger.warn(LOG_OWNER_NOT_FOUND, ex.getOwnerId());

        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                ex.getMessage(),
                request.getRequestURI(),
                null
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(
            Exception ex,
            HttpServletRequest request) {

        // Log full exception server-side only (not exposed to client)
        logger.error(LOG_UNEXPECTED_ERROR, ex);

        // Return generic message to client (no sensitive information)
        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                UNEXPECTED_ERROR_MESSAGE,
                request.getRequestURI(),
                null
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}
