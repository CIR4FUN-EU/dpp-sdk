package dppsdk.dpp4fun.validation;

import dppsdk.dpp4fun.model.Characteristics;
import dppsdk.dpp4fun.model.Dpp4Fun;
import dppsdk.core.model.DppCore;
import dppsdk.core.model.PassportMetadata;
import dppsdk.core.validation.ValidationException;
import dppsdk.dpp4fun.model.ProductClassification;
import dppsdk.support.TestDataFactory;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for Dpp4FunValidator - aggregate/cross-object rules.
 *
 * Rule summary:
 * - coreDpp, classification, and characteristics are required
 * - Delegates to all sub-validators
 * - Cross-rule: category vs productType substring consistency
 * - Cross-rule: externalDocumentationLink without Documentation object
 */
class Dpp4FunValidatorTest {

    private final Dpp4FunValidator validator = new Dpp4FunValidator();

    @Test
    void validate_validDpp_passes() {
        assertDoesNotThrow(() -> validator.validate(TestDataFactory.validDpp()));
    }

    @Test
    void validate_nullDpp_fails() {
        assertThrows(ValidationException.class, () -> validator.validate(null));
    }

    @Test
    void validate_missingCoreDppBuilder_fails() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                new Dpp4Fun.Builder()
                        .classification(TestDataFactory.validProductClassification())
                        .characteristics(TestDataFactory.validCharacteristics())
                        .build());

        assertTrue(ex.getMessage().contains("coreDpp"));
    }

    @Test
    void validate_missingClassificationBuilder_fails() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                new Dpp4Fun.Builder()
                        .coreDpp(TestDataFactory.validDppCore())
                        .characteristics(TestDataFactory.validCharacteristics())
                        .build());

        assertTrue(ex.getMessage().contains("classification"));
    }

    @Test
    void validate_missingCharacteristicsBuilder_fails() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                new Dpp4Fun.Builder()
                        .coreDpp(TestDataFactory.validDppCore())
                        .classification(TestDataFactory.validProductClassification())
                        .build());

        assertTrue(ex.getMessage().contains("characteristics"));
    }

    @Test
    void validate_nullBillOfMaterials_passes() {
        Dpp4Fun dpp = new Dpp4Fun.Builder()
                .coreDpp(TestDataFactory.validDppCore())
                .classification(TestDataFactory.validProductClassification())
                .characteristics(TestDataFactory.validCharacteristics())
                .build();

        assertDoesNotThrow(() -> validator.validate(dpp));
    }

    @Test
    void validate_categoryProductTypeMismatch_fails() {
        Dpp4Fun dpp = new Dpp4Fun.Builder()
                .coreDpp(new DppCore.Builder()
                        .passportMetadata(TestDataFactory.validPassportMetadata())
                        .nameplate(TestDataFactory.validNameplate())
                        .build())
                .classification(new ProductClassification.Builder()
                        .sector("Furniture").category("Beds").build())
                .characteristics(new Characteristics.Builder()
                        .productName("A Chair")
                        .productType("Chair")
                        .build())
                .build();

        ValidationException ex = assertThrows(ValidationException.class, () -> validator.validate(dpp));
        assertTrue(ex.getMessage().toLowerCase().contains("inconsistent") ||
                   ex.getMessage().toLowerCase().contains("contradict") ||
                   ex.getMessage().toLowerCase().contains("category"),
                "Expected cross-object consistency message, got: " + ex.getMessage());
    }

    @Test
    void validate_externalLinkWithoutDocumentation_fails() {
        Dpp4Fun dpp = new Dpp4Fun.Builder()
                .coreDpp(new DppCore.Builder()
                        .passportMetadata(new PassportMetadata.Builder()
                                .uniqueProductIdentifier(UUID.randomUUID())
                                .addPassportUpdateDate(LocalDate.now())
                                .externalDocumentationLink("https://docs.example.com")
                                .build())
                        .nameplate(TestDataFactory.validNameplate())
                        .build())
                .classification(TestDataFactory.validProductClassification())
                .characteristics(TestDataFactory.validCharacteristics())
                .build();

        ValidationException ex = assertThrows(ValidationException.class, () -> validator.validate(dpp));
        assertTrue(ex.getMessage().toLowerCase().contains("documentation") ||
                   ex.getMessage().toLowerCase().contains("link"),
                "Expected message about documentation/link inconsistency, got: " + ex.getMessage());
    }

    @Test
    void validate_categoryProductTypeCompatible_passes() {
        Dpp4Fun dpp = new Dpp4Fun.Builder()
                .coreDpp(new DppCore.Builder()
                        .passportMetadata(new PassportMetadata.Builder()
                                .uniqueProductIdentifier(UUID.randomUUID())
                                .addPassportUpdateDate(LocalDate.now())
                                .build())
                        .nameplate(TestDataFactory.validNameplate())
                        .build())
                .classification(new ProductClassification.Builder()
                        .sector("Furniture").category("Beds").build())
                .characteristics(new Characteristics.Builder()
                        .productName("Demo Bed")
                        .productType("Bed")
                        .build())
                .build();

        assertDoesNotThrow(() -> validator.validate(dpp));
    }

    @Test
    void dec005_validatesClassificationBeforeCharacteristicsAndCrossRules() {
        Dpp4Fun dpp = new Dpp4Fun.Builder()
                .coreDpp(TestDataFactory.validDppCore())
                .classification(new ProductClassification.Builder()
                        .sector("Furniture")
                        .category("Beds")
                        .addTag("Chair")
                        .addTag("chair")
                        .build())
                .characteristics(new Characteristics.Builder()
                        .productName("A Chair")
                        .productType("Table")
                        .build())
                .build();

        ValidationException error =
                assertThrows(ValidationException.class, () -> validator.validate(dpp));

        assertTrue(error.getMessage().contains("duplicate entry"));
    }
}

