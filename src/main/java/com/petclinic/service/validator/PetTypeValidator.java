package com.petclinic.service.validator;

import com.petclinic.domain.enums.PetType;
import com.petclinic.exception.InvalidPetTypeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Validator for pet type validation logic.
 */
@Component
public class PetTypeValidator {

    private static final Logger logger = LoggerFactory.getLogger(PetTypeValidator.class);

    /**
     * Validate pet type.
     *
     * @param petType Pet type to validate
     * @throws InvalidPetTypeException if pet type is invalid
     */
    public void validate(String petType) {
        if (!PetType.isValid(petType)) {
            logger.warn("Invalid pet type provided: {}", petType);
            throw new InvalidPetTypeException(petType);
        }
    }

    /**
     * Normalize pet type to lowercase.
     *
     * @param petType Pet type to normalize
     * @return Normalized pet type
     */
    public String normalize(String petType) {
        return petType.toLowerCase().trim();
    }
}
