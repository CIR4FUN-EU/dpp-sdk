package dppsdk.core.validation;

import dppsdk.core.model.Address;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ValidationUtilsUnicodeWhitespaceTest {

    @Test
    void structuralBuildersRejectEveryFrozenWhitespaceOnlyValue() {
        for (String whitespace : List.of(
                " ", "\t", "\n", "\u00a0", "\u202f", "\u2003")) {
            assertThrows(
                    IllegalArgumentException.class,
                    () -> new Address.Builder().country(whitespace).town("Town").build());
        }
    }

    @Test
    void zeroWidthSpaceAndMixedVisibleTextRemainNonBlank() {
        assertDoesNotThrow(
                () -> new Address.Builder().country("\u200b").town("Town").build());
        assertDoesNotThrow(
                () -> new Address.Builder().country("\u00a0visible\u2003").town("Town").build());
    }

    @Test
    void semanticListValidationUsesFrozenWhitespaceAndTrimTable() {
        assertThrows(
                ValidationException.class,
                () -> ValidationUtils.requireCleanStringList(
                        List.of("\u202f"), "ProductClassification.tags"));
        assertDoesNotThrow(
                () -> ValidationUtils.requireCleanStringList(
                        List.of("\u200b"), "ProductClassification.tags"));
        assertThrows(
                ValidationException.class,
                () -> ValidationUtils.requireCleanStringList(
                        List.of("Chair", "\u00a0chair\u2003"), "ProductClassification.tags"));
    }

    @Test
    void duplicateComparisonIsIndependentOfDefaultLocale() {
        Locale previous = Locale.getDefault();
        try {
            Locale.setDefault(Locale.forLanguageTag("tr-TR"));
            assertThrows(
                    ValidationException.class,
                    () -> ValidationUtils.requireCleanStringList(
                            List.of("I", "i"), "ProductClassification.tags"));
        } finally {
            Locale.setDefault(previous);
        }
    }

    @Test
    void numericSemanticChecksRejectNonFiniteValues() {
        assertDoesNotThrow(() -> ValidationUtils.requireNonNegative(0.0, "value"));
        assertDoesNotThrow(() -> ValidationUtils.requireNonNegative(1.25e2, "value"));
        for (double invalid : new double[] {
                -1.0, Double.NaN, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY}) {
            assertThrows(
                    ValidationException.class,
                    () -> ValidationUtils.requireNonNegative(invalid, "value"));
        }
    }
}
