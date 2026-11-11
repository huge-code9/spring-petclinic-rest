package com.petclinic.service.impl;

import com.petclinic.api.dto.PetRegistrationRequest;
import com.petclinic.api.dto.PetRegistrationResponse;
import com.petclinic.domain.entity.Pet;
import com.petclinic.domain.repository.OwnerRepository;
import com.petclinic.domain.repository.PetRepository;
import com.petclinic.exception.OwnerNotFoundException;
import com.petclinic.service.PetRegistrationService;
import com.petclinic.service.mapper.PetMapper;
import com.petclinic.service.validator.BirthDateValidator;
import com.petclinic.service.validator.PetTypeValidator;
import com.petclinic.util.LogSanitizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

/**
 * Implementation of PetRegistrationService.
 */
@Service
public class PetRegistrationServiceImpl implements PetRegistrationService {

    private static final Logger logger = LoggerFactory.getLogger(PetRegistrationServiceImpl.class);

    private final PetRepository petRepository;
    private final OwnerRepository ownerRepository;
    private final PetTypeValidator petTypeValidator;
    private final BirthDateValidator birthDateValidator;
    private final PetMapper petMapper;

    public PetRegistrationServiceImpl(
            PetRepository petRepository,
            OwnerRepository ownerRepository,
            PetTypeValidator petTypeValidator,
            BirthDateValidator birthDateValidator,
            PetMapper petMapper) {
        this.petRepository = petRepository;
        this.ownerRepository = ownerRepository;
        this.petTypeValidator = petTypeValidator;
        this.birthDateValidator = birthDateValidator;
        this.petMapper = petMapper;
    }

    @Override
    @Transactional
    public PetRegistrationResponse registerPet(PetRegistrationRequest request) {
        logger.info("Registering pet with name: {} for owner: {}",
                LogSanitizer.maskPetName(request.name()),
                LogSanitizer.maskOwnerId(request.ownerId()));

        // Validate birth date (service-layer validation)
        birthDateValidator.validate(request.birthDate());

        // Validate pet type
        petTypeValidator.validate(request.type());

        // Verify owner exists
        verifyOwnerExists(request.ownerId());

        // Create and save pet
        Pet pet = createPet(request);
        Pet savedPet = petRepository.save(pet);

        logger.info("Successfully registered pet with ID: {}", savedPet.getId());

        return petMapper.toResponse(savedPet);
    }

    private void verifyOwnerExists(String ownerId) {
        if (!ownerRepository.existsById(ownerId)) {
            logger.warn("Owner not found with ID: {}", LogSanitizer.maskOwnerId(ownerId));
            throw new OwnerNotFoundException(ownerId);
        }
    }

    private Pet createPet(PetRegistrationRequest request) {
        return new Pet(
                request.name(),
                petTypeValidator.normalize(request.type()),
                request.birthDate(),
                request.ownerId(),
                LocalDate.now()
        );
    }
}
