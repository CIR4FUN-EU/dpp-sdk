package dppsdk.dpp4fun.validation;

import dppsdk.core.validation.ValidationException;
import dppsdk.dpp4fun.model.BillOfMaterials;
import dppsdk.dpp4fun.model.Component;
import dppsdk.dpp4fun.model.Material;
import dppsdk.support.TestDataFactory;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for BillOfMaterialsValidator â€” semantic business rules.
 *
 * Rule summary:
 * - null BOM passes silently (BOM is optional)
 * - No null entries in any list
 * - Duplicate detection by name+reference combination
 * - mandatory material with portion=0 fails
 */
class BillOfMaterialsValidatorTest {

    private final BillOfMaterialsValidator validator = new BillOfMaterialsValidator();

    @Test
    void validate_nullBom_passes() {
        assertDoesNotThrow(() -> validator.validate(null));
    }

    @Test
    void validate_validBom_passes() {
        assertDoesNotThrow(() -> validator.validate(TestDataFactory.validBillOfMaterials()));
    }

    @Test
    void validate_nullMembers_failWithStableIndexedPaths() {
        BillOfMaterials materials = new BillOfMaterials.Builder()
                .addMaterial(null)
                .addMaterial(new Material.Builder().name("Steel").portion(1.0).build())
                .build();
        BillOfMaterials components = new BillOfMaterials.Builder()
                .addComponent(new Component.Builder().name("Frame").build())
                .addComponent(null)
                .build();

        ValidationException materialError =
                assertThrows(ValidationException.class, () -> validator.validate(materials));
        ValidationException componentError =
                assertThrows(ValidationException.class, () -> validator.validate(components));

        assertEquals("BillOfMaterials.materials[0] is null", materialError.getMessage());
        assertEquals("BillOfMaterials.components[1] is null", componentError.getMessage());
    }

    @Test
    void validate_duplicateMaterials_fails() {
        BillOfMaterials bom = new BillOfMaterials.Builder()
                .addMaterial(new Material.Builder().name("Steel").portion(2.0).reference("REF-1").build())
                .addMaterial(new Material.Builder().name("Steel").portion(3.0).reference("REF-1").build()) // duplicate name+ref
                .build();

        ValidationException ex = assertThrows(ValidationException.class, () -> validator.validate(bom));
        assertTrue(ex.getMessage().toLowerCase().contains("duplicate"),
                "Expected duplicate message, got: " + ex.getMessage());
    }

    @Test
    void validate_duplicateComponents_fails() {
        BillOfMaterials bom = new BillOfMaterials.Builder()
                .addComponent(new Component.Builder().name("Frame").reference("COMP-1").build())
                .addComponent(new Component.Builder().name("Frame").reference("COMP-1").build())
                .build();

        assertThrows(ValidationException.class, () -> validator.validate(bom));
    }

    @Test
    void validate_sameMaterialNameDifferentRef_passes() {
        // Same name, different reference â†’ not a duplicate
        BillOfMaterials bom = new BillOfMaterials.Builder()
                .addMaterial(new Material.Builder().name("Steel").portion(2.0).reference("REF-1").build())
                .addMaterial(new Material.Builder().name("Steel").portion(3.0).reference("REF-2").build())
                .build();
        assertDoesNotThrow(() -> validator.validate(bom));
    }
}

