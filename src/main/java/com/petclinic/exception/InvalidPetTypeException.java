package com.petclinic.exception;

/**
 * Exception thrown when pet type validation fails.
 */
public class InvalidPetTypeException extends RuntimeException {

    private final String petType;

    public InvalidPetTypeException(String petType) {
        super(String.format("Pet type '%s' is not valid. Valid types are: dog, cat, bird, rabbit, hamster, fish, reptile", petType));
        this.petType = petType;
    }

    public String getPetType() {
        return petType;
    }
}
