package dppsdk.dpp4fun.transport;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import dppsdk.dpp4fun.mapper.Dpp4FunMapper;
import dppsdk.dpp4fun.model.Characteristics;
import dppsdk.dpp4fun.model.Dimensions;
import dppsdk.dpp4fun.model.Dpp4Fun;
import dppsdk.dpp4fun.model.Material;
import dppsdk.dpp4fun.payload.Dpp4FunPayload;
import dppsdk.dpp4fun.validation.Dpp4FunValidationService;

/**
 * JSON transport facade for the DPP SDK.
 *
 * <p>This class only orchestrates Jackson serialization, payload mapping, and
 * optional validation. All field-by-field transformation remains in
 * {@code dppsdk.dpp4fun.mapper}.</p>
 */
public class Dpp4FunJsonCodec {

    private final ObjectMapper objectMapper;
    private final Dpp4FunMapper dppMapper;
    private final Dpp4FunValidationService validationService;

    public Dpp4FunJsonCodec() {
        this(defaultObjectMapper(), new Dpp4FunMapper(), new Dpp4FunValidationService());
    }

    private Dpp4FunJsonCodec(
            ObjectMapper objectMapper,
            Dpp4FunMapper dppMapper,
            Dpp4FunValidationService validationService) {
        this.objectMapper = objectMapper;
        this.dppMapper = dppMapper;
        this.validationService = validationService;
    }

    public String toJson(Dpp4Fun dpp) {
        try {
            requireFiniteNumbers(dpp);
            ObjectNode json = objectMapper.valueToTree(dppMapper.toPayload(dpp));
            flattenCoreDppForTransport(json);
            return objectMapper.writeValueAsString(json);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Failed to serialize DPP to JSON", e);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Failed to serialize DPP to JSON", e);
        }
    }

    public Dpp4Fun fromJson(String json) {
        try {
            JsonNode tree = objectMapper.readTree(json);
            if (!(tree instanceof ObjectNode objectNode)) {
                throw new IllegalArgumentException(
                        "Failed to deserialize DPP JSON",
                        new IllegalArgumentException("$ must be a JSON object"));
            }
            normalizeTransportShape(objectNode);
            Dpp4FunPayload payload =
                    objectMapper.treeToValue(tree, Dpp4FunPayload.class);
            return dppMapper.toDomain(payload);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Failed to deserialize DPP JSON", e);
        }
    }

    public Dpp4Fun fromJsonAndValidate(String json) {
        Dpp4Fun dpp = fromJson(json);
        validationService.validate(dpp);
        return dpp;
    }

    private static ObjectMapper defaultObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper;
    }

    private static void flattenCoreDppForTransport(ObjectNode root) {
        JsonNode core = root.remove("coreDpp");
        if (!(core instanceof ObjectNode coreNode)) {
            return;
        }

        moveIfPresent(coreNode, root, "passportMetadata");
        moveIfPresent(coreNode, root, "nameplate");
        moveIfPresent(coreNode, root, "documentation");
    }

    private static void normalizeTransportShape(ObjectNode root) {
        boolean hasCore = root.has("coreDpp") && root.get("coreDpp").isObject();
        ObjectNode coreNode = hasCore ? (ObjectNode) root.get("coreDpp") : null;

        if (!hasCore) {
            ObjectNode createdCore = root.putObject("coreDpp");
            moveIfPresent(root, createdCore, "passportMetadata");
            moveIfPresent(root, createdCore, "nameplate");
            moveIfPresent(root, createdCore, "documentation");

            if (createdCore.size() == 0) {
                root.remove("coreDpp");
            }
            return;
        }

        root.remove("passportMetadata");
        root.remove("nameplate");
        root.remove("documentation");

        if (coreNode.size() == 0) {
            root.remove("coreDpp");
        }
    }

    private static void moveIfPresent(ObjectNode source, ObjectNode target, String fieldName) {
        JsonNode value = source.remove(fieldName);
        if (value != null) {
            target.set(fieldName, value);
        }
    }

    private static void requireFiniteNumbers(Dpp4Fun dpp) {
        Characteristics characteristics = dpp.getCharacteristics();
        requireFinite(characteristics.getWeight(), "Characteristics.weight");
        Dimensions dimensions = characteristics.getDimensions();
        if (dimensions != null) {
            requireFinite(dimensions.getWidth(), "Dimensions.width");
            requireFinite(dimensions.getHeight(), "Dimensions.height");
            requireFinite(dimensions.getDepth(), "Dimensions.depth");
        }
        if (dpp.getBillOfMaterials() != null) {
            for (int index = 0; index < dpp.getBillOfMaterials().getMaterials().size(); index++) {
                Material material = dpp.getBillOfMaterials().getMaterials().get(index);
                requireFinite(
                        material.getPortion(),
                        "BillOfMaterials.materials[" + index + "].portion");
            }
        }
    }

    private static void requireFinite(Double value, String fieldName) {
        if (value != null && !Double.isFinite(value)) {
            throw new IllegalArgumentException(fieldName + " must be finite");
        }
    }
}



