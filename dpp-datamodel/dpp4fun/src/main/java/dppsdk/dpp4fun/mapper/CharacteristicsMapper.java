package dppsdk.dpp4fun.mapper;

import dppsdk.core.mapper.Mapper;
import dppsdk.core.mapper.MappingException;
import dppsdk.dpp4fun.model.Characteristics;
import dppsdk.dpp4fun.payload.CharacteristicsPayload;

import java.util.ArrayList;

/**
 * Maps between {@link Characteristics} and {@link CharacteristicsPayload}.
 * Delegates to {@link DimensionsMapper} for the nested dimensions.
 */
public class CharacteristicsMapper implements Mapper<Characteristics, CharacteristicsPayload> {

    private final DimensionsMapper dimensionsMapper = new DimensionsMapper();

    @Override
    public CharacteristicsPayload toPayload(Characteristics domain) {
        if (domain == null) return null;

        CharacteristicsPayload p = new CharacteristicsPayload();
        p.setProductName(domain.getProductName());
        p.setDescription(domain.getDescription());
        p.setBrand(domain.getBrand());
        p.setProductType(domain.getProductType());
        p.setDimensions(dimensionsMapper.toPayload(domain.getDimensions()));
        p.setWeight(domain.getWeight());
        p.setColor(domain.getColor());
        p.setFeatures(domain.getFeatures() != null ? new ArrayList<>(domain.getFeatures()) : null);
        return p;
    }

    @Override
    public Characteristics toDomain(CharacteristicsPayload payload) {
        if (payload == null) return null;

        try {
            Characteristics.Builder builder = new Characteristics.Builder();
            if (payload.getProductName() != null) {
                builder.productName(payload.getProductName());
            }
            if (payload.getDescription() != null) {
                builder.description(payload.getDescription());
            }
            if (payload.getBrand() != null) {
                builder.brand(payload.getBrand());
            }
            if (payload.getProductType() != null) {
                builder.productType(payload.getProductType());
            }
            if (payload.getDimensions() != null) {
                builder.dimensions(dimensionsMapper.toDomain(payload.getDimensions()));
            }
            if (payload.getWeight() != null) {
                builder.weight(payload.getWeight());
            }
            if (payload.getColor() != null) {
                builder.color(payload.getColor());
            }

            if (payload.getFeatures() != null) {
                for (int index = 0; index < payload.getFeatures().size(); index++) {
                    String feature = payload.getFeatures().get(index);
                    if (feature == null) {
                        throw new IllegalArgumentException(
                                "Characteristics.features[" + index + "] must not be null");
                    }
                    builder.addFeature(feature);
                }
            }

            return builder.build();
        } catch (MappingException e) {
            throw e;
        } catch (IllegalArgumentException e) {
            throw new MappingException(
                    "Failed to map CharacteristicsPayload to Characteristics: " + e.getMessage(), e);
        }
    }
}


