package dppsdk.core.model;

import dppsdk.core.util.ContractText;

import java.util.Objects;

/**
 * Represents reference links to external product documentation.
 *
 * Responsibilities:
 * - Provides safety and digital instruction links
 * - Defines retention timelines (availableForYears)
 *
 * Notes:
 * - Immutable value object
 * - Built via Builder
 */
public class Documentation {

    private final String digitalInstructionsLink;
    private final String safetyInstructionsLink;
    private final boolean downloadable;
    private final Integer availableForYears;
    private final boolean paperCopyAvailableOnRequest;

    private Documentation(Builder builder) {
        this.digitalInstructionsLink = builder.digitalInstructionsLink;
        this.safetyInstructionsLink = builder.safetyInstructionsLink;
        this.downloadable = builder.downloadable;
        this.availableForYears = builder.availableForYears;
        this.paperCopyAvailableOnRequest = builder.paperCopyAvailableOnRequest;
    }

    public String getDigitalInstructionsLink() {
        return digitalInstructionsLink;
    }

    public String getSafetyInstructionsLink() {
        return safetyInstructionsLink;
    }

    public boolean isDownloadable() {
        return downloadable;
    }

    public Integer getAvailableForYears() {
        return availableForYears;
    }

    public boolean isPaperCopyAvailableOnRequest() {
        return paperCopyAvailableOnRequest;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Documentation that = (Documentation) o;
        return downloadable == that.downloadable &&
                paperCopyAvailableOnRequest == that.paperCopyAvailableOnRequest &&
                Objects.equals(digitalInstructionsLink, that.digitalInstructionsLink) &&
                Objects.equals(safetyInstructionsLink, that.safetyInstructionsLink) &&
                Objects.equals(availableForYears, that.availableForYears);
    }

    @Override
    public int hashCode() {
        return Objects.hash(digitalInstructionsLink, safetyInstructionsLink, downloadable, availableForYears, paperCopyAvailableOnRequest);
    }

    @Override
    public String toString() {
        return "Documentation{" +
                "downloadable=" + downloadable +
                ", availableForYears=" + availableForYears +
                '}';
    }

    public Builder toBuilder() {
        return new Builder()
                .digitalInstructionsLink(this.digitalInstructionsLink)
                .safetyInstructionsLink(this.safetyInstructionsLink)
                .downloadable(this.downloadable)
                .availableForYears(this.availableForYears)
                .paperCopyAvailableOnRequest(this.paperCopyAvailableOnRequest);
    }

    public static class Builder {
        private String digitalInstructionsLink;
        private String safetyInstructionsLink;
        private boolean downloadable;
        private Integer availableForYears;
        private boolean paperCopyAvailableOnRequest;

        public Builder digitalInstructionsLink(String digitalInstructionsLink) {
            this.digitalInstructionsLink = digitalInstructionsLink;
            return this;
        }

        public Builder safetyInstructionsLink(String safetyInstructionsLink) {
            this.safetyInstructionsLink = safetyInstructionsLink;
            return this;
        }

        public Builder downloadable(boolean downloadable) {
            this.downloadable = downloadable;
            return this;
        }

        public Builder availableForYears(Integer availableForYears) {
            this.availableForYears = availableForYears;
            return this;
        }

        public Builder paperCopyAvailableOnRequest(boolean paperCopyAvailableOnRequest) {
            this.paperCopyAvailableOnRequest = paperCopyAvailableOnRequest;
            return this;
        }

        public Documentation build() {
            if (availableForYears != null && availableForYears < 0) {
                throw new IllegalArgumentException("availableForYears must be non-negative");
            }
            if (digitalInstructionsLink != null && ContractText.isBlank(digitalInstructionsLink)) {
                throw new IllegalArgumentException("digitalInstructionsLink must not be blank if provided");
            }
            if (safetyInstructionsLink != null && ContractText.isBlank(safetyInstructionsLink)) {
                throw new IllegalArgumentException("safetyInstructionsLink must not be blank if provided");
            }
            return new Documentation(this);
        }
    }
}


