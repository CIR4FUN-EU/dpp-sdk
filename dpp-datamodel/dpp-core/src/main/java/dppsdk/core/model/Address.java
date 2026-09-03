package dppsdk.core.model;

import dppsdk.core.util.ContractText;

import java.util.Objects;

/**
 * Represents a physical address for a Contact.
 *
 * Responsibilities:
 * - Stores country, town, region, street, and zip code
 *
 * Notes:
 * - Immutable value object
 * - Built via Builder
 */
public class Address {

    private final String country;
    private final String zipCode;
    private final String region;
    private final String town;
    private final String street;

    private Address(Builder builder) {
        this.country = builder.country;
        this.zipCode = builder.zipCode;
        this.region = builder.region;
        this.town = builder.town;
        this.street = builder.street;
    }

    public String getCountry() {
        return country;
    }

    public String getZipCode() {
        return zipCode;
    }

    public String getRegion() {
        return region;
    }

    public String getTown() {
        return town;
    }

    public String getStreet() {
        return street;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Address address = (Address) o;
        return Objects.equals(country, address.country) &&
                Objects.equals(zipCode, address.zipCode) &&
                Objects.equals(region, address.region) &&
                Objects.equals(town, address.town) &&
                Objects.equals(street, address.street);
    }

    @Override
    public int hashCode() {
        return Objects.hash(country, zipCode, region, town, street);
    }

    @Override
    public String toString() {
        return "Address{" +
                "country='" + country + '\'' +
                ", zipCode='" + zipCode + '\'' +
                ", town='" + town + '\'' +
                ", street='" + street + '\'' +
                '}';
    }

    public Builder toBuilder() {
        return new Builder()
                .country(this.country)
                .zipCode(this.zipCode)
                .region(this.region)
                .town(this.town)
                .street(this.street);
    }

    public static class Builder {
        private String country;
        private String zipCode;
        private String region;
        private String town;
        private String street;

        public Builder country(String country) {
            this.country = country;
            return this;
        }

        public Builder zipCode(String zipCode) {
            this.zipCode = zipCode;
            return this;
        }

        public Builder region(String region) {
            this.region = region;
            return this;
        }

        public Builder town(String town) {
            this.town = town;
            return this;
        }

        public Builder street(String street) {
            this.street = street;
            return this;
        }

        public Address build() {
            if (ContractText.isBlank(country)) {
                throw new IllegalArgumentException("country is required");
            }
            if (ContractText.isBlank(town)) {
                throw new IllegalArgumentException("town is required");
            }
            if (zipCode != null && ContractText.isBlank(zipCode)) {
                throw new IllegalArgumentException("zipCode must not be blank if provided");
            }
            if (region != null && ContractText.isBlank(region)) {
                throw new IllegalArgumentException("region must not be blank if provided");
            }
            if (street != null && ContractText.isBlank(street)) {
                throw new IllegalArgumentException("street must not be blank if provided");
            }
            return new Address(this);
        }
    }
}



