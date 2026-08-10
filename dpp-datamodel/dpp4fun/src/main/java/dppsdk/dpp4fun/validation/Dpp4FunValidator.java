package dppsdk.dpp4fun.validation;

import dppsdk.core.validation.DppCoreValidator;
import dppsdk.core.validation.ValidationException;
import dppsdk.core.validation.ValidationUtils;
import dppsdk.core.validation.Validator;
import dppsdk.core.util.ContractText;
import dppsdk.dpp4fun.model.Dpp4Fun;

/**
 * Top-level aggregate validator for Dpp4Fun.
 *
 * <p>Responsibilities:</p>
 * <ul>
 *   <li>ensure required submodels exist</li>
 *   <li>delegate to typed subvalidators</li>
 *   <li>enforce cross-object consistency rules</li>
 * </ul>
 */
public class Dpp4FunValidator implements Validator<Dpp4Fun> {

    private final DppCoreValidator coreDppValidator = new DppCoreValidator();
    private final ProductClassificationValidator classificationValidator = new ProductClassificationValidator();
    private final CharacteristicsValidator characteristicsValidator = new CharacteristicsValidator();
    private final BillOfMaterialsValidator billOfMaterialsValidator = new BillOfMaterialsValidator();

    @Override
    public void validate(Dpp4Fun dpp) throws ValidationException {
        if (dpp == null) {
            throw new ValidationException("Dpp4Fun cannot be null");
        }

        validateRequiredFields(dpp);

        coreDppValidator.validate(dpp.getCoreDpp());
        classificationValidator.validate(dpp.getClassification());
        characteristicsValidator.validate(dpp.getCharacteristics());
        billOfMaterialsValidator.validate(dpp.getBillOfMaterials());

        validateCrossRules(dpp);
    }

    private void validateRequiredFields(Dpp4Fun dpp) throws ValidationException {
        ValidationUtils.requireNotNull(dpp.getCoreDpp(), "Dpp4Fun.coreDpp");
        ValidationUtils.requireNotNull(dpp.getClassification(), "Dpp4Fun.classification");
        ValidationUtils.requireNotNull(dpp.getCharacteristics(), "Dpp4Fun.characteristics");
    }

    private void validateCrossRules(Dpp4Fun dpp) throws ValidationException {
        String category = dpp.getCategory();
        String productType = dpp.getProductType();
        if (ValidationUtils.hasText(category) && ValidationUtils.hasText(productType)) {
            String catLower = ContractText.normalizeForComparison(category);
            String typeLower = ContractText.normalizeForComparison(productType);
            if (!catLower.contains(typeLower) && !typeLower.contains(catLower)) {
                throw new ValidationException(
                        "Cross-object validation: classification.category '" + category
                                + "' and characteristics.productType '" + productType
                                + "' appear inconsistent");
            }
        }

        if (dpp.getDocumentation() == null
                && ValidationUtils.hasText(dpp.getExternalDocumentationLink())) {
            throw new ValidationException(
                    "Cross-object validation: PassportMetadata has an externalDocumentationLink "
                            + "but no Documentation object is present in the DPP");
        }
    }
}
