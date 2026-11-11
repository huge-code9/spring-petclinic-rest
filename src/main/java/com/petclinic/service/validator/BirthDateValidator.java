package com.petclinic.service.validator;

import com.petclinic.exception.InvalidBirthDateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

/**
 * Validator for birth date validation logic.
 */
@Component
public class BirthDateValidator {

    private static final Logger logger = LoggerFactory.getLogger(BirthDateValidator.class);

    /**
     * Validate that birth date is not in the future.
     *
     * @param birthDate Birth date to validate
     * @throws InvalidBirthDateException if birth date is in the future
     */
    public void validate(LocalDate birthDate) {
        if (birthDate.isAfter(LocalDate.now())) {
            logger.warn("Birth date in future: {}", birthDate);
            throw new InvalidBirthDateException(birthDate);
        }
    }
}
