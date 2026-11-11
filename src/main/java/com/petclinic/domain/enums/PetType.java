package com.petclinic.domain.enums;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Enumeration of valid pet types.
 */
public enum PetType {
    DOG("dog"),
    CAT("cat"),
    BIRD("bird"),
    RABBIT("rabbit"),
    HAMSTER("hamster"),
    FISH("fish"),
    REPTILE("reptile");

    private final String value;

    PetType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    /**
     * Get all valid pet type values.
     *
     * @return Set of valid pet type strings
     */
    public static Set<String> getValidValues() {
        return Arrays.stream(values())
                .map(PetType::getValue)
                .collect(Collectors.toSet());
    }

    /**
     * Check if a pet type string is valid.
     *
     * @param type Pet type string
     * @return true if valid, false otherwise
     */
    public static boolean isValid(String type) {
        if (type == null || type.isBlank()) {
            return false;
        }
        String normalizedType = type.toLowerCase().trim();
        return getValidValues().contains(normalizedType);
    }

    /**
     * Get PetType enum from string value.
     *
     * @param type Pet type string
     * @return PetType enum
     * @throws IllegalArgumentException if type is invalid
     */
    public static PetType fromString(String type) {
        String normalizedType = type.toLowerCase().trim();
        return Arrays.stream(values())
                .filter(pt -> pt.getValue().equals(normalizedType))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid pet type: " + type));
    }
}
