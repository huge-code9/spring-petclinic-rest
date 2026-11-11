package com.petclinic.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Error response DTO for validation and business errors.
 *
 * @param timestamp Error timestamp
 * @param status HTTP status code
 * @param error Error type
 * @param message Error message
 * @param path Request path
 * @param validationErrors List of validation errors (optional)
 */
@Schema(description = "Error response")
public record ErrorResponse(

        @Schema(description = "Error timestamp", example = "2024-01-15T10:30:00")
        LocalDateTime timestamp,

        @Schema(description = "HTTP status code", example = "400")
        int status,

        @Schema(description = "Error type", example = "Bad Request")
        String error,

        @Schema(description = "Error message", example = "Validation failed")
        String message,

        @Schema(description = "Request path", example = "/api/v1/pets")
        String path,

        @Schema(description = "List of validation errors")
        List<ValidationError> validationErrors
) {

    /**
     * Validation error detail.
     *
     * @param field Field name
     * @param rejectedValue Rejected value
     * @param message Error message
     */
    @Schema(description = "Validation error detail")
    public record ValidationError(
            @Schema(description = "Field name", example = "name")
            String field,

            @Schema(description = "Rejected value", example = "")
            Object rejectedValue,

            @Schema(description = "Error message", example = "Pet name cannot be empty")
            String message
    ) {
    }
}
