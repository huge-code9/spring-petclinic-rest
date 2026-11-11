package com.petclinic.api.controller;

import com.petclinic.api.dto.ErrorResponse;
import com.petclinic.api.dto.PetRegistrationRequest;
import com.petclinic.api.dto.PetRegistrationResponse;
import com.petclinic.service.PetRegistrationService;
import com.petclinic.util.LogSanitizer;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for pet registration operations.
 */
@RestController
@RequestMapping("/api/v1/pets")
@Tag(name = "Pet Registration", description = "API for registering pets")
public class PetRegistrationController {

    private static final Logger logger = LoggerFactory.getLogger(PetRegistrationController.class);

    private final PetRegistrationService petRegistrationService;

    public PetRegistrationController(PetRegistrationService petRegistrationService) {
        this.petRegistrationService = petRegistrationService;
    }

    @PostMapping
    @Operation(
            summary = "Register a new pet",
            description = "Register a new pet with owner details. Pet name, type, and birth date are required. Birth date cannot be in the future."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Pet successfully registered",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PetRegistrationResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request - validation error or invalid pet type",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Owner not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    public ResponseEntity<PetRegistrationResponse> registerPet(
            @Valid @RequestBody PetRegistrationRequest request) {

        logger.info("Received pet registration request for owner: {}",
                LogSanitizer.maskOwnerId(request.ownerId()));

        PetRegistrationResponse response = petRegistrationService.registerPet(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
