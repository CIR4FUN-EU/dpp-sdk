package dppsdk.dpp4fun.model;

import dppsdk.core.util.ContractText;

import java.util.Objects;

/**
 * Represents a raw substance or material within the BOM.
 *
 * Responsibilities:
 * - Captures material composition name and portion value
 * - Indicates mandatory presence
 *
 * Notes:
 * - Immutable value object
 * - Built via Builder
 */
public class Material {

    private final String name;
    private final boolean mandatory;
    private final double portion;
    private final String reference;

    private Material(Builder builder) {
        this.name = builder.name;
        this.mandatory = builder.mandatory;
        this.portion = builder.portion;
        this.reference = builder.reference;
    }

    public String getName() {
        return name;
    }

    public boolean isMandatory() {
        return mandatory;
    }

    public double getPortion() {
        return portion;
    }

    public String getReference() {
        return reference;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Material material = (Material) o;
        return mandatory == material.mandatory &&
                Double.compare(material.portion, portion) == 0 &&
                Objects.equals(name, material.name) &&
                Objects.equals(reference, material.reference);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, mandatory, portion, reference);
    }

    @Override
    public String toString() {
        return "Material{" +
                "name='" + name + '\'' +
                ", mandatory=" + mandatory +
                ", portion=" + portion +
                '}';
    }

    public Builder toBuilder() {
        return new Builder()
                .name(this.name)
                .mandatory(this.mandatory)
                .portion(this.portion)
                .reference(this.reference);
    }

    public static class Builder {
        private String name;
        private boolean mandatory;
        private double portion;
        private String reference;

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder mandatory(boolean mandatory) {
            this.mandatory = mandatory;
            return this;
        }

        public Builder portion(double portion) {
            this.portion = portion;
            return this;
        }

        public Builder reference(String reference) {
            this.reference = reference;
            return this;
        }

        public Material build() {
            if (ContractText.isBlank(name)) {
                throw new IllegalArgumentException("name is required");
            }
            if (!Double.isFinite(portion) || portion < 0) {
                throw new IllegalArgumentException("portion must be finite and non-negative");
            }
            if (reference != null && ContractText.isBlank(reference)) {
                throw new IllegalArgumentException("reference must not be blank if provided");
            }
            return new Material(this);
        }
    }
}



