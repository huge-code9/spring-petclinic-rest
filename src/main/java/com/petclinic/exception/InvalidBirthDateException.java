package com.petclinic.exception;

import java.time.LocalDate;

/**
 * Exception thrown when birth date validation fails.
 */
public class InvalidBirthDateException extends RuntimeException {

    private final LocalDate birthDate;

    public InvalidBirthDateException(LocalDate birthDate) {
        super(String.format("Birth date '%s' cannot be in the future", birthDate));
        this.birthDate = birthDate;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }
}
