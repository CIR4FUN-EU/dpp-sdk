package dppsdk.dpp4fun.model;

import dppsdk.core.util.ContractText;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents the core characteristics and features of a product.
 *
 * Responsibilities:
 * - Holds product name and dimension data
 * - Maintains lists of product features
 *
 * Notes:
 * - Immutable value object
 * - Built via Builder
 */
public class Characteristics {

    private final String productName;
    private final String description;
    private final String brand;
    private final String productType;
    private final Dimensions dimensions;
    private final Double weight;
    private final String color;
    private final List<String> features;

    private Characteristics(Builder builder) {
        this.productName = builder.productName;
        this.description = builder.description;
        this.brand = builder.brand;
        this.productType = builder.productType;
        this.dimensions = builder.dimensions;
        this.weight = builder.weight;
        this.color = builder.color;
        this.features = builder.features != null ? new ArrayList<>(builder.features) : new ArrayList<>();
    }

    public String getProductName() {
        return productName;
    }

    public String getDescription() {
        return description;
    }

    public String getBrand() {
        return brand;
    }

    public String getProductType() {
        return productType;
    }

    public Dimensions getDimensions() {
        return dimensions;
    }

    public Double getWeight() {
        return weight;
    }

    public String getColor() {
        return color;
    }

    public List<String> getFeatures() {
        return new ArrayList<>(features);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Characteristics that = (Characteristics) o;
        return Objects.equals(productName, that.productName) &&
                Objects.equals(description, that.description) &&
                Objects.equals(brand, that.brand) &&
                Objects.equals(productType, that.productType) &&
                Objects.equals(dimensions, that.dimensions) &&
                Objects.equals(weight, that.weight) &&
                Objects.equals(color, that.color) &&
                Objects.equals(features, that.features);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productName, description, brand, productType, dimensions, weight, color, features);
    }

    @Override
    public String toString() {
        return "Characteristics{" +
                "productName='" + productName + '\'' +
                ", brand='" + brand + '\'' +
                ", productType='" + productType + '\'' +
                '}';
    }

    public Builder toBuilder() {
        Builder builder = new Builder()
                .productName(this.productName)
                .description(this.description)
                .brand(this.brand)
                .productType(this.productType)
                .dimensions(this.dimensions)
                .weight(this.weight)
                .color(this.color);
        if (this.features != null && !this.features.isEmpty()) {
            this.features.forEach(builder::addFeature);
        }
        return builder;
    }

    public static class Builder {
        private String productName;
        private String description;
        private String brand;
        private String productType;
        private Dimensions dimensions;
        private Double weight;
        private String color;
        private List<String> features;

        public Builder productName(String productName) {
            this.productName = productName;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder brand(String brand) {
            this.brand = brand;
            return this;
        }

        public Builder productType(String productType) {
            this.productType = productType;
            return this;
        }

        public Builder dimensions(Dimensions dimensions) {
            this.dimensions = dimensions;
            return this;
        }

        public Builder weight(Double weight) {
            this.weight = weight;
            return this;
        }

        public Builder color(String color) {
            this.color = color;
            return this;
        }

        public Builder features(List<String> features) {
            this.features = features;
            return this;
        }

        public Builder addFeature(String feature) {
            if (this.features == null) {
                this.features = new ArrayList<>();
            }
            this.features.add(feature);
            return this;
        }

        public Builder removeFeature(String feature) {
            if (this.features != null) {
                this.features.remove(feature);
            }
            return this;
        }

        public Characteristics build() {
            if (ContractText.isBlank(productName)) {
                throw new IllegalArgumentException("productName is required");
            }
            if (description != null && ContractText.isBlank(description)) {
                throw new IllegalArgumentException("description must not be blank if provided");
            }
            if (brand != null && ContractText.isBlank(brand)) {
                throw new IllegalArgumentException("brand must not be blank if provided");
            }
            if (productType != null && ContractText.isBlank(productType)) {
                throw new IllegalArgumentException("productType must not be blank if provided");
            }
            if (color != null && ContractText.isBlank(color)) {
                throw new IllegalArgumentException("color must not be blank if provided");
            }
            if (weight != null && (!Double.isFinite(weight) || weight < 0)) {
                throw new IllegalArgumentException("weight must be finite and non-negative");
            }
            return new Characteristics(this);
        }
    }
}




