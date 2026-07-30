package dppsdk.dpp4fun.model;

import dppsdk.core.util.ContractText;

import java.util.Objects;

/**
 * Represents a distinct assembled item within the BOM.
 *
 * Responsibilities:
 * - Captures component name and optional reference identifiers
 *
 * Notes:
 * - Immutable value object
 * - Built via Builder
 */
public class Component {

    private final String name;
    private final String reference;

    private Component(Builder builder) {
        this.name = builder.name;
        this.reference = builder.reference;
    }

    public String getName() {
        return name;
    }

    public String getReference() {
        return reference;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Component component = (Component) o;
        return Objects.equals(name, component.name) &&
                Objects.equals(reference, component.reference);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, reference);
    }

    @Override
    public String toString() {
        return "Component{" +
                "name='" + name + '\'' +
                ", reference='" + reference + '\'' +
                '}';
    }

    public Builder toBuilder() {
        return new Builder()
                .name(this.name)
                .reference(this.reference);
    }

    public static class Builder {
        private String name;
        private String reference;

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder reference(String reference) {
            this.reference = reference;
            return this;
        }

        public Component build() {
            if (ContractText.isBlank(name)) {
                throw new IllegalArgumentException("name is required");
            }
            if (reference != null && ContractText.isBlank(reference)) {
                throw new IllegalArgumentException("reference must not be blank if provided");
            }
            return new Component(this);
        }
    }
}



