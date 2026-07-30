package dppsdk.core.model;

import dppsdk.core.util.ContractText;

import java.util.Objects;

/**
 * Represents the regulatory identity and involved parties of a product.
 *
 * Responsibilities:
 * - Associates products with GTIN and local article numbers
 * - Links manufacturer and supplier organizations
 *
 * Notes:
 * - Immutable value object
 * - Built via Builder
 */
public class Nameplate {

    private final String gtinCode;
    private final String internalArticleNumber;
    private final String batchNumber;
    private final String customsTariffNumber;
    private final String uriOfTheProduct;
    private final Organization manufacturer;
    private final Organization supplier;

    private Nameplate(Builder builder) {
        this.gtinCode = builder.gtinCode;
        this.internalArticleNumber = builder.internalArticleNumber;
        this.batchNumber = builder.batchNumber;
        this.customsTariffNumber = builder.customsTariffNumber;
        this.uriOfTheProduct = builder.uriOfTheProduct;
        this.manufacturer = builder.manufacturer;
        this.supplier = builder.supplier;
    }

    public String getGtinCode() {
        return gtinCode;
    }

    public String getInternalArticleNumber() {
        return internalArticleNumber;
    }

    public String getBatchNumber() {
        return batchNumber;
    }

    public String getCustomsTariffNumber() {
        return customsTariffNumber;
    }

    public String getUriOfTheProduct() {
        return uriOfTheProduct;
    }

    public Organization getManufacturer() {
        return manufacturer;
    }

    public Organization getSupplier() {
        return supplier;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Nameplate nameplate = (Nameplate) o;
        return Objects.equals(gtinCode, nameplate.gtinCode) &&
                Objects.equals(internalArticleNumber, nameplate.internalArticleNumber) &&
                Objects.equals(batchNumber, nameplate.batchNumber) &&
                Objects.equals(customsTariffNumber, nameplate.customsTariffNumber) &&
                Objects.equals(uriOfTheProduct, nameplate.uriOfTheProduct) &&
                Objects.equals(manufacturer, nameplate.manufacturer) &&
                Objects.equals(supplier, nameplate.supplier);
    }

    @Override
    public int hashCode() {
        return Objects.hash(gtinCode, internalArticleNumber, batchNumber, customsTariffNumber,
                uriOfTheProduct, manufacturer, supplier);
    }

    @Override
    public String toString() {
        return "Nameplate{" +
                "gtinCode='" + gtinCode + '\'' +
                ", internalArticleNumber='" + internalArticleNumber + '\'' +
                ", batchNumber='" + batchNumber + '\'' +
                '}';
    }

    public Builder toBuilder() {
        return new Builder()
                .gtinCode(this.gtinCode)
                .internalArticleNumber(this.internalArticleNumber)
                .batchNumber(this.batchNumber)
                .customsTariffNumber(this.customsTariffNumber)
                .uriOfTheProduct(this.uriOfTheProduct)
                .manufacturer(this.manufacturer)
                .supplier(this.supplier);
    }

    public static class Builder {
        private String gtinCode;
        private String internalArticleNumber;
        private String batchNumber;
        private String customsTariffNumber;
        private String uriOfTheProduct;
        private Organization manufacturer;
        private Organization supplier;

        public Builder gtinCode(String gtinCode) {
            this.gtinCode = gtinCode;
            return this;
        }

        public Builder internalArticleNumber(String internalArticleNumber) {
            this.internalArticleNumber = internalArticleNumber;
            return this;
        }

        public Builder batchNumber(String batchNumber) {
            this.batchNumber = batchNumber;
            return this;
        }

        public Builder customsTariffNumber(String customsTariffNumber) {
            this.customsTariffNumber = customsTariffNumber;
            return this;
        }

        public Builder uriOfTheProduct(String uriOfTheProduct) {
            this.uriOfTheProduct = uriOfTheProduct;
            return this;
        }

        public Builder manufacturer(Organization manufacturer) {
            this.manufacturer = manufacturer;
            return this;
        }

        public Builder supplier(Organization supplier) {
            this.supplier = supplier;
            return this;
        }

        public Nameplate build() {
            if (ContractText.isBlank(gtinCode)) {
                throw new IllegalArgumentException("gtinCode is required");
            }
            if (internalArticleNumber != null && ContractText.isBlank(internalArticleNumber)) {
                throw new IllegalArgumentException("internalArticleNumber must not be blank if provided");
            }
            if (batchNumber != null && ContractText.isBlank(batchNumber)) {
                throw new IllegalArgumentException("batchNumber must not be blank if provided");
            }
            if (customsTariffNumber != null && ContractText.isBlank(customsTariffNumber)) {
                throw new IllegalArgumentException("customsTariffNumber must not be blank if provided");
            }
            if (uriOfTheProduct != null && ContractText.isBlank(uriOfTheProduct)) {
                throw new IllegalArgumentException("uriOfTheProduct must not be blank if provided");
            }
            return new Nameplate(this);
        }
    }
}



