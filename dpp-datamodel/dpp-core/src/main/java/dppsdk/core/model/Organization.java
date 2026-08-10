package dppsdk.core.model;

import dppsdk.core.util.ContractText;

import java.util.Objects;

/**
 * Represents a business entity involved with the product.
 *
 * Responsibilities:
 * - Holds corporate details like GLN and URIs
 * - Contains Contact details for the entity
 *
 * Notes:
 * - Immutable value object
 * - Built via Builder
 */
public class Organization {

    private final String name;
    private final String gln;
    private final String productDescription;
    private final String productDesignation;
    private final String productFamily;
    private final String productRoot;
    private final String productOrderSuffix;
    private final String uri;
    private final Contact contact;
    private final OrganizationRole role;

    private Organization(Builder builder) {
        this.name = builder.name;
        this.gln = builder.gln;
        this.productDescription = builder.productDescription;
        this.productDesignation = builder.productDesignation;
        this.productFamily = builder.productFamily;
        this.productRoot = builder.productRoot;
        this.productOrderSuffix = builder.productOrderSuffix;
        this.uri = builder.uri;
        this.contact = builder.contact;
        this.role = builder.role;
    }

    public String getName() {
        return name;
    }

    public String getGln() {
        return gln;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public String getProductDesignation() {
        return productDesignation;
    }

    public String getProductFamily() {
        return productFamily;
    }

    public String getProductRoot() {
        return productRoot;
    }

    public String getProductOrderSuffix() {
        return productOrderSuffix;
    }

    public String getUri() {
        return uri;
    }

    public Contact getContact() {
        return contact;
    }

    public OrganizationRole getRole() {
        return role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Organization that = (Organization) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(gln, that.gln) &&
                Objects.equals(productDescription, that.productDescription) &&
                Objects.equals(productDesignation, that.productDesignation) &&
                Objects.equals(productFamily, that.productFamily) &&
                Objects.equals(productRoot, that.productRoot) &&
                Objects.equals(productOrderSuffix, that.productOrderSuffix) &&
                Objects.equals(uri, that.uri) &&
                Objects.equals(contact, that.contact) &&
                role == that.role;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, gln, productDescription, productDesignation, productFamily,
                productRoot, productOrderSuffix, uri, contact, role);
    }

    @Override
    public String toString() {
        return "Organization{" +
                "name='" + name + '\'' +
                ", gln='" + gln + '\'' +
                ", role=" + role +
                ", uri='" + uri + '\'' +
                '}';
    }

    public Builder toBuilder() {
        return new Builder()
                .name(this.name)
                .gln(this.gln)
                .productDescription(this.productDescription)
                .productDesignation(this.productDesignation)
                .productFamily(this.productFamily)
                .productRoot(this.productRoot)
                .productOrderSuffix(this.productOrderSuffix)
                .uri(this.uri)
                .contact(this.contact)
                .role(this.role);
    }

    public static class Builder {
        private String name;
        private String gln;
        private String productDescription;
        private String productDesignation;
        private String productFamily;
        private String productRoot;
        private String productOrderSuffix;
        private String uri;
        private Contact contact;
        private OrganizationRole role;

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder gln(String gln) {
            this.gln = gln;
            return this;
        }

        public Builder productDescription(String productDescription) {
            this.productDescription = productDescription;
            return this;
        }

        public Builder productDesignation(String productDesignation) {
            this.productDesignation = productDesignation;
            return this;
        }

        public Builder productFamily(String productFamily) {
            this.productFamily = productFamily;
            return this;
        }

        public Builder productRoot(String productRoot) {
            this.productRoot = productRoot;
            return this;
        }

        public Builder productOrderSuffix(String productOrderSuffix) {
            this.productOrderSuffix = productOrderSuffix;
            return this;
        }

        public Builder uri(String uri) {
            this.uri = uri;
            return this;
        }

        public Builder contact(Contact contact) {
            this.contact = contact;
            return this;
        }

        public Builder role(OrganizationRole role) {
            this.role = role;
            return this;
        }

        public Organization build() {
            if (ContractText.isBlank(name)) {
                throw new IllegalArgumentException("Organization name is required");
            }
            if (gln != null && ContractText.isBlank(gln)) {
                throw new IllegalArgumentException("gln must not be blank if provided");
            }
            if (productDescription != null && ContractText.isBlank(productDescription)) {
                throw new IllegalArgumentException("productDescription must not be blank if provided");
            }
            if (productDesignation != null && ContractText.isBlank(productDesignation)) {
                throw new IllegalArgumentException("productDesignation must not be blank if provided");
            }
            if (productFamily != null && ContractText.isBlank(productFamily)) {
                throw new IllegalArgumentException("productFamily must not be blank if provided");
            }
            if (productRoot != null && ContractText.isBlank(productRoot)) {
                throw new IllegalArgumentException("productRoot must not be blank if provided");
            }
            if (productOrderSuffix != null && ContractText.isBlank(productOrderSuffix)) {
                throw new IllegalArgumentException("productOrderSuffix must not be blank if provided");
            }
            if (uri != null && ContractText.isBlank(uri)) {
                throw new IllegalArgumentException("uri must not be blank if provided");
            }
            return new Organization(this);
        }
    }
}



