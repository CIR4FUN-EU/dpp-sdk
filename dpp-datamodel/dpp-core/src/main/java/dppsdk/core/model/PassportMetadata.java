package dppsdk.core.model;

import dppsdk.core.util.ContractText;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Represents technical metadata identifying the DPP itself.
 *
 * Responsibilities:
 * - Tracks unique product identifiers
 * - Manages lifecycle update dates and digital tag links
 *
 * Notes:
 * - Immutable value object
 * - Built via Builder
 */
public class PassportMetadata {

    private final UUID uniqueProductIdentifier;
    private final List<LocalDate> passportUpdateDates;
    private final String qrCodeOrDigitalTag;
    private final String externalDocumentationLink;

    private PassportMetadata(Builder builder) {
        this.uniqueProductIdentifier = builder.uniqueProductIdentifier;
        this.passportUpdateDates = new ArrayList<>(builder.passportUpdateDates);
        this.qrCodeOrDigitalTag = builder.qrCodeOrDigitalTag;
        this.externalDocumentationLink = builder.externalDocumentationLink;
    }

    public UUID getUniqueProductIdentifier() {
        return uniqueProductIdentifier;
    }

    public List<LocalDate> getPassportUpdateDates() {
        return new ArrayList<>(passportUpdateDates);
    }

    public String getQrCodeOrDigitalTag() {
        return qrCodeOrDigitalTag;
    }

    public String getExternalDocumentationLink() {
        return externalDocumentationLink;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PassportMetadata that = (PassportMetadata) o;
        return Objects.equals(uniqueProductIdentifier, that.uniqueProductIdentifier) &&
                Objects.equals(passportUpdateDates, that.passportUpdateDates) &&
                Objects.equals(qrCodeOrDigitalTag, that.qrCodeOrDigitalTag) &&
                Objects.equals(externalDocumentationLink, that.externalDocumentationLink);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uniqueProductIdentifier, passportUpdateDates, qrCodeOrDigitalTag, externalDocumentationLink);
    }

    @Override
    public String toString() {
        return "PassportMetadata{" +
                "uniqueProductIdentifier=" + uniqueProductIdentifier +
                ", passportUpdateDates=" + passportUpdateDates +
                ", qrCodeOrDigitalTag='" + qrCodeOrDigitalTag + '\'' +
                ", externalDocumentationLink='" + externalDocumentationLink + '\'' +
                '}';
    }

    public Builder toBuilder() {
        Builder builder = new Builder()
                .uniqueProductIdentifier(this.uniqueProductIdentifier)
                .qrCodeOrDigitalTag(this.qrCodeOrDigitalTag)
                .externalDocumentationLink(this.externalDocumentationLink);
        if (this.passportUpdateDates != null && !this.passportUpdateDates.isEmpty()) {
            this.passportUpdateDates.forEach(builder::addPassportUpdateDate);
        }
        return builder;
    }

    public static class Builder {
        private UUID uniqueProductIdentifier;
        private List<LocalDate> passportUpdateDates;
        private String qrCodeOrDigitalTag;
        private String externalDocumentationLink;

        public Builder uniqueProductIdentifier(UUID uniqueProductIdentifier) {
            this.uniqueProductIdentifier = uniqueProductIdentifier;
            return this;
        }

        public Builder addPassportUpdateDate(LocalDate updateDate) {
            if (this.passportUpdateDates == null) {
                this.passportUpdateDates = new ArrayList<>();
            }
            if (updateDate == null) {
                throw new IllegalArgumentException("passportUpdateDates entries must not be null");
            }
            this.passportUpdateDates.add(updateDate);
            return this;
        }

        public Builder removePassportUpdateDate(LocalDate updateDate) {
            if (this.passportUpdateDates != null) {
                this.passportUpdateDates.remove(updateDate);
            }
            return this;
        }

        public Builder qrCodeOrDigitalTag(String qrCodeOrDigitalTag) {
            this.qrCodeOrDigitalTag = qrCodeOrDigitalTag;
            return this;
        }

        public Builder externalDocumentationLink(String externalDocumentationLink) {
            this.externalDocumentationLink = externalDocumentationLink;
            return this;
        }

        public PassportMetadata build() {
            // Validate uniqueProductIdentifier
            if (uniqueProductIdentifier == null) {
                throw new IllegalArgumentException("uniqueProductIdentifier must not be null");
            }

            // Validate passportUpdateDates - list not null/empty
            if (passportUpdateDates == null || passportUpdateDates.isEmpty()) {
                throw new IllegalArgumentException("passportUpdateDates must not be null or empty");
            }

            // Validate qrCodeOrDigitalTag - not blank if provided
            if (qrCodeOrDigitalTag != null && ContractText.isBlank(qrCodeOrDigitalTag)) {
                throw new IllegalArgumentException("qrCodeOrDigitalTag must not be blank if provided");
            }

            // Validate externalDocumentationLink - not blank if provided
            if (externalDocumentationLink != null && ContractText.isBlank(externalDocumentationLink)) {
                throw new IllegalArgumentException("externalDocumentationLink must not be blank if provided");
            }

            return new PassportMetadata(this);
        }
    }
}




