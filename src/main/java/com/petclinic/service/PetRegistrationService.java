package com.petclinic.service;

import com.petclinic.api.dto.PetRegistrationRequest;
import com.petclinic.api.dto.PetRegistrationResponse;

/**
 * Service interface for pet registration operations.
 */
public interface PetRegistrationService {

    /**
     * Register a new pet with the provided details.
     *
     * @param request Pet registration request
     * @return Pet registration response
     * @throws com.petclinic.exception.OwnerNotFoundException if owner does not exist
     * @throws com.petclinic.exception.InvalidPetTypeException if pet type is invalid
     * @throws com.petclinic.exception.InvalidBirthDateException if birth date is in the future
     */
    PetRegistrationResponse registerPet(PetRegistrationRequest request);
}
