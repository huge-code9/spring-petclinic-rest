package com.petclinic.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

/**
 * Response DTO for pet registration.
 *
 * @param id Unique identifier of the registered pet
 * @param name Pet name
 * @param type Pet type
 * @param birthDate Pet birth date
 * @param ownerId Owner identifier
 * @param registeredAt Registration timestamp
 */
@Schema(description = "Pet registration response")
public record PetRegistrationResponse(

        @Schema(description = "Unique identifier of the pet", example = "pet-456")
        String id,

        @Schema(description = "Name of the pet", example = "Fluffy")
        String name,

        @Schema(description = "Type of the pet", example = "dog")
        String type,

        @Schema(description = "Birth date of the pet", example = "2020-05-15")
        LocalDate birthDate,

        @Schema(description = "Identifier of the pet owner", example = "owner-123")
        String ownerId,

        @Schema(description = "Registration timestamp", example = "2024-01-15")
        LocalDate registeredAt
) {
}
