package com.petclinic.exception;

/**
 * Exception thrown when an owner is not found in the system.
 */
public class OwnerNotFoundException extends RuntimeException {

    private final String ownerId;

    public OwnerNotFoundException(String ownerId) {
        super(String.format("Owner with ID '%s' does not exist", ownerId));
        this.ownerId = ownerId;
    }

    public String getOwnerId() {
        return ownerId;
    }
}
