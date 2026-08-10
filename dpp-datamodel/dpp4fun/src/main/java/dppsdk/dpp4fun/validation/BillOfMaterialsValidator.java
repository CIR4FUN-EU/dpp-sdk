package dppsdk.dpp4fun.validation;

import dppsdk.core.validation.ValidationException;
import dppsdk.core.util.ContractText;
import dppsdk.core.validation.Validator;
import dppsdk.dpp4fun.model.BillOfMaterials;
import dppsdk.dpp4fun.model.Material;
import dppsdk.dpp4fun.model.Component;
import dppsdk.dpp4fun.model.Part;
import java.util.List;
import java.util.HashSet;
import java.util.Set;

/**
 * Validator for BillOfMaterials.
 * Business rules:
 * - if BOM exists, validate all materials/components/parts
 * - no null entries in any list
 * - detect duplicates by name+reference combination
 */
public class BillOfMaterialsValidator implements Validator<BillOfMaterials> {
    
    private final MaterialValidator materialValidator = new MaterialValidator();
    private final ComponentValidator componentValidator = new ComponentValidator();
    private final PartValidator partValidator = new PartValidator();

    @Override
    public void validate(BillOfMaterials bom) throws ValidationException {
        if (bom == null) {
            return; // BillOfMaterials is optional
        }

        // Validate materials
        List<Material> materials = bom.getMaterials();
        if (materials != null) {
            Set<String> materialKeys = new HashSet<>();
            for (int i = 0; i < materials.size(); i++) {
                Material material = materials.get(i);
                if (material == null) {
                    throw new ValidationException("BillOfMaterials.materials[" + i + "] is null");
                }
                materialValidator.validate(material);

                // Duplicate detection by name+reference
                String key = toKey(material.getName(), material.getReference());
                if (!materialKeys.add(key)) {
                    throw new ValidationException(
                        "BillOfMaterials.materials contains duplicate entry: " + key
                    );
                }
            }
        }

        // Validate components
        List<Component> components = bom.getComponents();
        if (components != null) {
            Set<String> componentKeys = new HashSet<>();
            for (int i = 0; i < components.size(); i++) {
                Component component = components.get(i);
                if (component == null) {
                    throw new ValidationException("BillOfMaterials.components[" + i + "] is null");
                }
                componentValidator.validate(component);

                // Duplicate detection by name+reference
                String key = toKey(component.getName(), component.getReference());
                if (!componentKeys.add(key)) {
                    throw new ValidationException(
                        "BillOfMaterials.components contains duplicate entry: " + key
                    );
                }
            }
        }

        // Validate parts
        List<Part> parts = bom.getParts();
        if (parts != null) {
            Set<String> partKeys = new HashSet<>();
            for (int i = 0; i < parts.size(); i++) {
                Part part = parts.get(i);
                if (part == null) {
                    throw new ValidationException("BillOfMaterials.parts[" + i + "] is null");
                }
                partValidator.validate(part);

                // Duplicate detection by name+reference
                String key = toKey(part.getName(), part.getReference());
                if (!partKeys.add(key)) {
                    throw new ValidationException(
                        "BillOfMaterials.parts contains duplicate entry: " + key
                    );
                }
            }
        }
    }

    /**
     * Creates a composite key from name and reference for duplicate detection.
     */
    private String toKey(String name, String reference) {
        String n = name != null ? ContractText.normalizeForComparison(name) : "";
        String r = reference != null ? ContractText.normalizeForComparison(reference) : "";
        return n + "|" + r;
    }
}


