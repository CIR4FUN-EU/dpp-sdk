package dppsdk.core.model;

import dppsdk.core.util.ContractText;

import java.util.Objects;

/**
 * Represents a telephone contact number.
 *
 * Responsibilities:
 * - Captures the telephone string and an optional type context
 *
 * Notes:
 * - Immutable value object
 * - Built via Builder
 */
public class Telephone {

    private final String telephoneNumber;
    private final String typeOfTelephone;

    private Telephone(Builder builder) {
        this.telephoneNumber = builder.telephoneNumber;
        this.typeOfTelephone = builder.typeOfTelephone;
    }

    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    public String getTypeOfTelephone() {
        return typeOfTelephone;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Telephone telephone = (Telephone) o;
        return Objects.equals(telephoneNumber, telephone.telephoneNumber) &&
                Objects.equals(typeOfTelephone, telephone.typeOfTelephone);
    }

    @Override
    public int hashCode() {
        return Objects.hash(telephoneNumber, typeOfTelephone);
    }

    @Override
    public String toString() {
        return "Telephone{" +
                "telephoneNumber='" + telephoneNumber + '\'' +
                ", typeOfTelephone='" + typeOfTelephone + '\'' +
                '}';
    }

    public Builder toBuilder() {
        return new Builder()
                .telephoneNumber(this.telephoneNumber)
                .typeOfTelephone(this.typeOfTelephone);
    }

    public static class Builder {
        private String telephoneNumber;
        private String typeOfTelephone;

        public Builder telephoneNumber(String telephoneNumber) {
            this.telephoneNumber = telephoneNumber;
            return this;
        }

        public Builder typeOfTelephone(String typeOfTelephone) {
            this.typeOfTelephone = typeOfTelephone;
            return this;
        }

        public Telephone build() {
            if (ContractText.isBlank(telephoneNumber)) {
                throw new IllegalArgumentException("telephoneNumber is required");
            }
            if (typeOfTelephone != null && ContractText.isBlank(typeOfTelephone)) {
                throw new IllegalArgumentException("typeOfTelephone must not be blank if provided");
            }
            return new Telephone(this);
        }
    }
}

