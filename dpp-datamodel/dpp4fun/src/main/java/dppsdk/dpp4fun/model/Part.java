package dppsdk.dpp4fun.model;

import dppsdk.core.util.ContractText;

import java.util.Objects;

/**
 * Represents an individual piece forming a component or product in the BOM.
 *
 * Responsibilities:
 * - Captures part name, reference, and mandatory presence flag
 *
 * Notes:
 * - Immutable value object
 * - Built via Builder
 */
public class Part {

    private final String name;
    private final boolean mandatory;
    private final String reference;

    private Part(Builder builder) {
        this.name = builder.name;
        this.mandatory = builder.mandatory;
        this.reference = builder.reference;
    }

    public String getName() {
        return name;
    }

    public boolean isMandatory() {
        return mandatory;
    }

    public String getReference() {
        return reference;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Part part = (Part) o;
        return mandatory == part.mandatory &&
                Objects.equals(name, part.name) &&
                Objects.equals(reference, part.reference);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, mandatory, reference);
    }

    @Override
    public String toString() {
        return "Part{" +
                "name='" + name + '\'' +
                ", mandatory=" + mandatory +
                '}';
    }

    public Builder toBuilder() {
        return new Builder()
                .name(this.name)
                .mandatory(this.mandatory)
                .reference(this.reference);
    }

    public static class Builder {
        private String name;
        private boolean mandatory;
        private String reference;

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder mandatory(boolean mandatory) {
            this.mandatory = mandatory;
            return this;
        }

        public Builder reference(String reference) {
            this.reference = reference;
            return this;
        }

        public Part build() {
            if (ContractText.isBlank(name)) {
                throw new IllegalArgumentException("name is required");
            }
            if (reference != null && ContractText.isBlank(reference)) {
                throw new IllegalArgumentException("reference must not be blank if provided");
            }
            return new Part(this);
        }
    }
}



