package com.alibaba.smart.framework.engine.common.util;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.alibaba.smart.framework.engine.exception.ValidationException;

/**
 * ID type converter utility class.
 *
 * <p>Provides unified ID type conversion between String (external API) and Long (database storage).
 * Centralizes all conversion logic to eliminate scattered Long.valueOf() calls and improve error handling.
 *
 * @author SmartEngine Team
 * @since 1.x.x
 */
public final class IdConverter {

    private IdConverter() {
        // Utility class, prevent instantiation
    }

    // ============ String → Long Conversion ============

    /**
     * Convert String ID to Long.
     *
     * @param id String ID
     * @return Long ID, or null if input is null
     * @throws ValidationException if ID format is invalid
     */
    public static Long toLong(String id) {
        if (id == null) {
            return null;
        }
        try {
            return Long.parseLong(id.trim());
        } catch (NumberFormatException e) {
            throw new ValidationException("Invalid ID format: '" + id + "'. Expected a valid numeric ID.");
        }
    }

    /**
     * Convert String ID to Long with field name for better error message.
     *
     * @param id String ID
     * @param fieldName field name for error message
     * @return Long ID, or null if input is null
     * @throws ValidationException if ID format is invalid
     */
    public static Long toLong(String id, String fieldName) {
        if (id == null) {
            return null;
        }
        try {
            return Long.parseLong(id.trim());
        } catch (NumberFormatException e) {
            throw new ValidationException(
                "Invalid " + fieldName + " format: '" + id + "'. Expected a valid numeric ID.");
        }
    }

    /**
     * Safe conversion, returns null instead of throwing exception on failure.
     *
     * @param id String ID
     * @return Long ID, or null if input is null or invalid
     */
    public static Long toLongOrNull(String id) {
        if (id == null) {
            return null;
        }
        try {
            return Long.parseLong(id.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * Batch convert String ID list to Long list.
     *
     * <p>Returns null if input is null (important for MyBatis condition checks),
     * returns empty list if input is empty.
     *
     * @param ids String ID list
     * @return Long ID list, null if input is null, empty list if input is empty
     * @throws ValidationException if any ID format is invalid
     */
    public static List<Long> toLongList(List<String> ids) {
        if (ids == null) {
            return null;
        }
        if (ids.isEmpty()) {
            return new ArrayList<>();
        }
        return ids.stream()
                .map(IdConverter::toLong)
                .collect(Collectors.toList());
    }

    // ============ Long → String Conversion ============

    /**
     * Convert Long ID to String.
     *
     * @param id Long ID
     * @return String ID, or null if input is null
     */
    public static String toString(Long id) {
        return id != null ? id.toString() : null;
    }

    /**
     * Batch convert Long ID list to String list.
     *
     * @param ids Long ID list
     * @return String ID list, or empty list if input is null/empty
     */
    public static List<String> toStringList(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return new ArrayList<>();
        }
        return ids.stream()
                .map(IdConverter::toString)
                .collect(Collectors.toList());
    }

    // ============ Validation Methods ============

    /**
     * Check if string is a valid Long ID.
     *
     * @param id String ID
     * @return true if valid numeric ID, false otherwise
     */
    public static boolean isValidLongId(String id) {
        if (id == null || id.trim().isEmpty()) {
            return false;
        }
        try {
            Long.parseLong(id.trim());
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Validate ID format, throw exception if invalid.
     *
     * @param id String ID
     * @param fieldName field name for error message
     * @throws ValidationException if ID is not null and format is invalid
     */
    public static void validateLongId(String id, String fieldName) {
        if (id != null && !isValidLongId(id)) {
            throw new ValidationException(
                "Invalid " + fieldName + ": '" + id + "' is not a valid numeric ID");
        }
    }
}
