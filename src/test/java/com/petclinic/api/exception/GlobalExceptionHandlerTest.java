package com.petclinic.api.exception;

import com.petclinic.api.dto.ErrorResponse;
import com.petclinic.exception.InvalidBirthDateException;
import com.petclinic.exception.InvalidPetTypeException;
import com.petclinic.exception.OwnerNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void handleValidationException_returns400WithValidationErrors() {
        BindingResult bindingResult = new BeanPropertyBindingResult(new Object(), "petRegistrationRequest");
        bindingResult.addError(new FieldError("petRegistrationRequest", "name", "", false, null, null, "Pet name cannot be empty"));
        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(null, bindingResult);
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/api/v1/pets");

        ResponseEntity<ErrorResponse> response = handler.handleValidationException(ex, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        ErrorResponse body = response.getBody();
        assertThat(body).isNotNull();
        assertThat(body.status()).isEqualTo(400);
        assertThat(body.error()).isEqualTo("Bad Request");
        assertThat(body.message()).contains("Validation failed");
        assertThat(body.path()).isEqualTo("/api/v1/pets");
        assertThat(body.validationErrors()).isNotNull();
        assertThat(body.validationErrors()).hasSize(1);
    }

    @Test
    void handleInvalidPetTypeException_returns400() {
        InvalidPetTypeException ex = new InvalidPetTypeException("dragon");
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/api/v1/pets");

        ResponseEntity<ErrorResponse> response = handler.handleInvalidPetTypeException(ex, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        ErrorResponse body = response.getBody();
        assertThat(body).isNotNull();
        assertThat(body.status()).isEqualTo(400);
        assertThat(body.message()).contains("dragon");
        assertThat(body.validationErrors()).isNull();
    }

    @Test
    void handleOwnerNotFoundException_returns404() {
        OwnerNotFoundException ex = new OwnerNotFoundException("owner-999");
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/api/v1/pets");

        ResponseEntity<ErrorResponse> response = handler.handleOwnerNotFoundException(ex, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        ErrorResponse body = response.getBody();
        assertThat(body).isNotNull();
        assertThat(body.status()).isEqualTo(404);
        assertThat(body.message()).contains("owner-999");
    }

    @Test
    void handleInvalidBirthDateException_returns400() {
        InvalidBirthDateException ex = new InvalidBirthDateException(LocalDate.now().plusDays(1));
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/api/v1/pets");

        ResponseEntity<ErrorResponse> response = handler.handleInvalidBirthDateException(ex, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        ErrorResponse body = response.getBody();
        assertThat(body).isNotNull();
        assertThat(body.status()).isEqualTo(400);
        assertThat(body.message()).contains("cannot be in the future");
    }

    @Test
    void handleGenericException_returns500() {
        Exception ex = new Exception("Unexpected error");
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/api/v1/pets");

        ResponseEntity<ErrorResponse> response = handler.handleGenericException(ex, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        ErrorResponse body = response.getBody();
        assertThat(body).isNotNull();
        assertThat(body.status()).isEqualTo(500);
        // generic message
        assertThat(body.message()).contains("unexpected", "try again", "later");
    }
}
