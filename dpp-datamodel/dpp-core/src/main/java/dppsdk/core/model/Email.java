package dppsdk.core.model;

import dppsdk.core.util.ContractText;

import java.util.Objects;

/**
 * Represents an electronic mail address.
 *
 * Responsibilities:
 * - Captures the email string and an optional type context
 *
 * Notes:
 * - Immutable value object
 * - Built via Builder
 */
public class Email {

    private final String emailAddress;
    private final String typeOfEmail;

    private Email(Builder builder) {
        this.emailAddress = builder.emailAddress;
        this.typeOfEmail = builder.typeOfEmail;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public String getTypeOfEmail() {
        return typeOfEmail;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Email email = (Email) o;
        return Objects.equals(emailAddress, email.emailAddress) &&
                Objects.equals(typeOfEmail, email.typeOfEmail);
    }

    @Override
    public int hashCode() {
        return Objects.hash(emailAddress, typeOfEmail);
    }

    @Override
    public String toString() {
        return "Email{" +
                "emailAddress='" + emailAddress + '\'' +
                ", typeOfEmail='" + typeOfEmail + '\'' +
                '}';
    }

    public Builder toBuilder() {
        return new Builder()
                .emailAddress(this.emailAddress)
                .typeOfEmail(this.typeOfEmail);
    }

    public static class Builder {
        private String emailAddress;
        private String typeOfEmail;

        public Builder emailAddress(String emailAddress) {
            this.emailAddress = emailAddress;
            return this;
        }

        public Builder typeOfEmail(String typeOfEmail) {
            this.typeOfEmail = typeOfEmail;
            return this;
        }

        public Email build() {
            if (ContractText.isBlank(emailAddress)) {
                throw new IllegalArgumentException("emailAddress is required");
            }
            if (typeOfEmail != null && ContractText.isBlank(typeOfEmail)) {
                throw new IllegalArgumentException("typeOfEmail must not be blank if provided");
            }
            return new Email(this);
        }
    }
}

