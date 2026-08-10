package dppsdk.core.model;

import dppsdk.core.util.ContractText;

import java.util.Objects;

/**
 * Represents communication channels for an Organization.
 *
 * Responsibilities:
 * - Holds address, email, and telephone objects
 * - Tracks the specific organizational unit or contact name
 *
 * Notes:
 * - Immutable value object
 * - Built via Builder
 */
public class Contact {

    private final String organization;
    private final Address address;
    private final Email email;
    private final Telephone telephone;

    private Contact(Builder builder) {
        this.organization = builder.organization;
        this.address = builder.address;
        this.email = builder.email;
        this.telephone = builder.telephone;
    }

    public String getOrganization() {
        return organization;
    }

    public Address getAddress() {
        return address;
    }

    public Email getEmail() {
        return email;
    }

    public Telephone getTelephone() {
        return telephone;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Contact contact = (Contact) o;
        return Objects.equals(organization, contact.organization) &&
                Objects.equals(address, contact.address) &&
                Objects.equals(email, contact.email) &&
                Objects.equals(telephone, contact.telephone);
    }

    @Override
    public int hashCode() {
        return Objects.hash(organization, address, email, telephone);
    }

    @Override
    public String toString() {
        return "Contact{" +
                "organization='" + organization + '\'' +
                ", address=" + address +
                '}';
    }

    public Builder toBuilder() {
        return new Builder()
                .organization(this.organization)
                .address(this.address)
                .email(this.email)
                .telephone(this.telephone);
    }

    public static class Builder {
        private String organization;
        private Address address;
        private Email email;
        private Telephone telephone;

        public Builder organization(String organization) {
            this.organization = organization;
            return this;
        }

        public Builder address(Address address) {
            this.address = address;
            return this;
        }

        public Builder email(Email email) {
            this.email = email;
            return this;
        }

        public Builder telephone(Telephone telephone) {
            this.telephone = telephone;
            return this;
        }

        public Contact build() {
            if (ContractText.isBlank(organization)) {
                throw new IllegalArgumentException("organization is required");
            }
            return new Contact(this);
        }
    }
}



