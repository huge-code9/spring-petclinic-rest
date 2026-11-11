package com.petclinic.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

/**
 * Request DTO for pet registration.
 *
 * @param name Pet name (required, non-blank, max 50 characters)
 * @param type Pet type (required, non-blank, max 30 characters)
 * @param birthDate Pet birth date (required, cannot be in the future)
 * @param ownerId Owner identifier (required, non-blank)
 */
@Schema(description = "Pet registration request")
public record PetRegistrationRequest(

        @NotBlank(message = "Pet name cannot be empty")
        @Size(max = 50, message = "Pet name cannot exceed 50 characters")
        @Schema(description = "Name of the pet", example = "Fluffy", required = true)
        String name,

        @NotBlank(message = "Pet type cannot be empty")
        @Size(max = 30, message = "Pet type cannot exceed 30 characters")
        @Schema(description = "Type of the pet (e.g., dog, cat, bird)", example = "dog", required = true)
        String type,

        @NotNull(message = "Birth date cannot be null")
        @PastOrPresent(message = "Birth date cannot be in the future")
        @Schema(description = "Birth date of the pet", example = "2020-05-15", required = true)
        LocalDate birthDate,

        @NotBlank(message = "Owner ID cannot be empty")
        @Schema(description = "Identifier of the pet owner", example = "owner-123", required = true)
        String ownerId
) {
}
