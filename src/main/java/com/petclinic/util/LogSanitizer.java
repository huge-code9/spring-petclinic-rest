package com.petclinic.util;

/**
 * Utility class for sanitizing sensitive data in logs.
 */
public final class LogSanitizer {

    private static final int VISIBLE_CHARS = 6;
    private static final String MASK = "***";

    private LogSanitizer() {
        // Utility class - prevent instantiation
    }

    /**
     * Mask owner ID for logging purposes.
     * Shows first 6 characters followed by ***.
     *
     * @param ownerId Owner ID to mask
     * @return Masked owner ID
     */
    public static String maskOwnerId(String ownerId) {
        if (ownerId == null || ownerId.isEmpty()) {
            return MASK;
        }
        if (ownerId.length() <= VISIBLE_CHARS) {
            return MASK;
        }
        return ownerId.substring(0, VISIBLE_CHARS) + MASK;
    }

    /**
     * Mask pet name for logging purposes.
     * Shows first character followed by ***.
     *
     * @param petName Pet name to mask
     * @return Masked pet name
     */
    public static String maskPetName(String petName) {
        if (petName == null || petName.isEmpty()) {
            return MASK;
        }
        if (petName.length() == 1) {
            return MASK;
        }
        return petName.charAt(0) + MASK;
    }
}
