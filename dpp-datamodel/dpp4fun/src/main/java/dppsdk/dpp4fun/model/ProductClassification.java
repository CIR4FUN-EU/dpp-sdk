package dppsdk.dpp4fun.model;

import dppsdk.core.util.ContractText;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents the industry taxonomy mapping for a product.
 *
 * Responsibilities:
 * - Categorizes the product into logical sectors and groups
 * - Holds descriptive tags
 *
 * Notes:
 * - Immutable value object
 * - Built via Builder
 */
public class ProductClassification {

    private final String sector;
    private final String group;
    private final String category;
    private final String subCategory;
    private final List<String> tags;

    private ProductClassification(Builder builder) {
        this.sector = builder.sector;
        this.group = builder.group;
        this.category = builder.category;
        this.subCategory = builder.subCategory;
        this.tags = builder.tags != null ? new ArrayList<>(builder.tags) : new ArrayList<>();
    }

    public String getSector() {
        return sector;
    }

    public String getGroup() {
        return group;
    }

    public String getCategory() {
        return category;
    }

    public String getSubCategory() {
        return subCategory;
    }

    public List<String> getTags() {
        return new ArrayList<>(tags);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductClassification that = (ProductClassification) o;
        return Objects.equals(sector, that.sector) &&
                Objects.equals(group, that.group) &&
                Objects.equals(category, that.category) &&
                Objects.equals(subCategory, that.subCategory) &&
                Objects.equals(tags, that.tags);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sector, group, category, subCategory, tags);
    }

    @Override
    public String toString() {
        return "ProductClassification{" +
                "sector='" + sector + '\'' +
                ", group='" + group + '\'' +
                ", category='" + category + '\'' +
                '}';
    }

    public Builder toBuilder() {
        Builder builder = new Builder()
                .sector(this.sector)
                .group(this.group)
                .category(this.category)
                .subCategory(this.subCategory);
        if (this.tags != null && !this.tags.isEmpty()) {
            this.tags.forEach(builder::addTag);
        }
        return builder;
    }

    public static class Builder {
        private String sector;
        private String group;
        private String category;
        private String subCategory;
        private List<String> tags;

        public Builder sector(String sector) {
            this.sector = sector;
            return this;
        }

        public Builder group(String group) {
            this.group = group;
            return this;
        }

        public Builder category(String category) {
            this.category = category;
            return this;
        }

        public Builder subCategory(String subCategory) {
            this.subCategory = subCategory;
            return this;
        }

        public Builder tags(List<String> tags) {
            this.tags = tags;
            return this;
        }

        public Builder addTag(String tag) {
            if (this.tags == null) {
                this.tags = new ArrayList<>();
            }
            this.tags.add(tag);
            return this;
        }

        public Builder removeTag(String tag) {
            if (this.tags != null) {
                this.tags.remove(tag);
            }
            return this;
        }

        public ProductClassification build() {
            if (ContractText.isBlank(sector)) {
                throw new IllegalArgumentException("sector is required");
            }
            if (ContractText.isBlank(category)) {
                throw new IllegalArgumentException("category is required");
            }
            if (group != null && ContractText.isBlank(group)) {
                throw new IllegalArgumentException("group must not be blank if provided");
            }
            if (subCategory != null && ContractText.isBlank(subCategory)) {
                throw new IllegalArgumentException("subCategory must not be blank if provided");
            }
            return new ProductClassification(this);
        }
    }
}




