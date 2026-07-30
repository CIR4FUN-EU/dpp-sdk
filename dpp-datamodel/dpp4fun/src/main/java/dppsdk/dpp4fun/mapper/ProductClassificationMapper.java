package dppsdk.dpp4fun.mapper;

import dppsdk.core.mapper.Mapper;
import dppsdk.core.mapper.MappingException;
import dppsdk.dpp4fun.model.ProductClassification;
import dppsdk.dpp4fun.payload.ProductClassificationPayload;

import java.util.ArrayList;
import java.util.List;

/**
 * Maps between {@link ProductClassification} and {@link ProductClassificationPayload}.
 */
public class ProductClassificationMapper implements Mapper<ProductClassification, ProductClassificationPayload> {

    @Override
    public ProductClassificationPayload toPayload(ProductClassification domain) {
        if (domain == null) return null;

        ProductClassificationPayload p = new ProductClassificationPayload();
        p.setSector(domain.getSector());
        p.setGroup(domain.getGroup());
        p.setCategory(domain.getCategory());
        p.setSubCategory(domain.getSubCategory());
        p.setTags(domain.getTags() != null ? new ArrayList<>(domain.getTags()) : null);
        return p;
    }

    @Override
    public ProductClassification toDomain(ProductClassificationPayload payload) {
        if (payload == null) return null;

        try {
            ProductClassification.Builder builder = new ProductClassification.Builder();
            if (payload.getSector() != null) {
                builder.sector(payload.getSector());
            }
            if (payload.getGroup() != null) {
                builder.group(payload.getGroup());
            }
            if (payload.getCategory() != null) {
                builder.category(payload.getCategory());
            }
            if (payload.getSubCategory() != null) {
                builder.subCategory(payload.getSubCategory());
            }

            if (payload.getTags() != null) {
                for (int index = 0; index < payload.getTags().size(); index++) {
                    String tag = payload.getTags().get(index);
                    if (tag == null) {
                        throw new IllegalArgumentException(
                                "ProductClassification.tags[" + index + "] must not be null");
                    }
                    builder.addTag(tag);
                }
            }

            return builder.build();
        } catch (IllegalArgumentException e) {
            throw new MappingException(
                    "Failed to map ProductClassificationPayload to ProductClassification: " + e.getMessage(), e);
        }
    }
}


