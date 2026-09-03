package dppsdk.core.validation;

import dppsdk.core.util.ContractText;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Utility methods for validation.
 * Provides common validation checks with clear error messages.
 * Used by all validators to keep code consistent and DRY.
 */
public final class ValidationUtils {
    
    private ValidationUtils() {
        // Utility class - no instances
    }

    /**
     * Returns true if the string is non-null and not blank.
     */
    public static boolean hasText(String value) {
        return value != null && !ContractText.isBlank(value);
    }

    /**
     * Require a non-null value.
     *
     * @param value The value to check
     * @param fieldName The name of the field (for error message)
     * @throws ValidationException if value is null
     */
    public static void requireNotNull(Object value, String fieldName) throws ValidationException {
        if (value == null) {
            throw new ValidationException(fieldName + " is required");
        }
    }

    /**
     * Require a non-blank string.
     *
     * @param value The string to check
     * @param fieldName The name of the field (for error message)
     * @throws ValidationException if value is null or blank
     */
    public static void requireNotBlank(String value, String fieldName) throws ValidationException {
        if (ContractText.isBlank(value)) {
            throw new ValidationException(fieldName + " must not be blank");
        }
    }

    /**
     * Require a condition to be true.
     *
     * @param condition The condition to check
     * @param message The error message if condition is false
     * @throws ValidationException if condition is false
     */
    public static void requireTrue(boolean condition, String message) throws ValidationException {
        if (!condition) {
            throw new ValidationException(message);
        }
    }

    /**
     * Require a non-negative number.
     *
     * @param value The number to check
     * @param fieldName The name of the field (for error message)
     * @throws ValidationException if value is negative
     */
    public static void requireNonNegative(double value, String fieldName) throws ValidationException {
        if (!Double.isFinite(value) || value < 0) {
            throw new ValidationException(fieldName + " must be finite and non-negative, but got " + value);
        }
    }

    /**
     * Require a non-negative number (nullable).
     *
     * @param value The number to check (null is allowed)
     * @param fieldName The name of the field (for error message)
     * @throws ValidationException if value is negative
     */
    public static void requireNonNegativeIfPresent(Double value, String fieldName) throws ValidationException {
        if (value != null && (!Double.isFinite(value) || value < 0)) {
            throw new ValidationException(fieldName + " must be finite and non-negative, but got " + value);
        }
    }

    /**
     * Require a non-negative integer (nullable).
     *
     * @param value The number to check (null is allowed)
     * @param fieldName The name of the field (for error message)
     * @throws ValidationException if value is negative
     */
    public static void requireNonNegativeIfPresent(Integer value, String fieldName) throws ValidationException {
        if (value != null && value < 0) {
            throw new ValidationException(fieldName + " must be non-negative, but got " + value);
        }
    }

    /**
     * Check a list of strings for null, blank, or duplicate entries.
     *
     * @param items The list to check
     * @param listName The name of the list (for error messages)
     * @throws ValidationException if any entry is null, blank, or duplicated
     */
    public static void requireCleanStringList(List<String> items, String listName) throws ValidationException {
        if (items == null) {
            return;
        }
        Set<String> seen = new HashSet<>();
        for (int i = 0; i < items.size(); i++) {
            String item = items.get(i);
            if (item == null) {
                throw new ValidationException(listName + "[" + i + "] must not be null");
            }
            if (ContractText.isBlank(item)) {
                throw new ValidationException(listName + "[" + i + "] must not be blank");
            }
            String normalized = ContractText.normalizeForComparison(item);
            if (!seen.add(normalized)) {
                throw new ValidationException(listName + " contains duplicate entry: '" + ContractText.strip(item) + "'");
            }
        }
    }
}

