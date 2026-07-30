package dppsdk.dpp4fun.model;

import dppsdk.core.util.ContractText;

import java.util.Objects;

/**
 * Represents physical boundaries/dimensions of a product.
 *
 * Responsibilities:
 * - Captures width, height, and depth
 * - Associates measurements with a unit
 *
 * Notes:
 * - Immutable value object
 * - Built via Builder
 */
public class Dimensions {

    private final Double width;
    private final Double height;
    private final Double depth;
    private final String unit;

    private Dimensions(Builder builder) {
        this.width = builder.width;
        this.height = builder.height;
        this.depth = builder.depth;
        this.unit = builder.unit;
    }

    public Double getWidth() {
        return width;
    }

    public Double getHeight() {
        return height;
    }

    public Double getDepth() {
        return depth;
    }

    public String getUnit() {
        return unit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Dimensions that = (Dimensions) o;
        return Objects.equals(width, that.width) &&
                Objects.equals(height, that.height) &&
                Objects.equals(depth, that.depth) &&
                Objects.equals(unit, that.unit);
    }

    @Override
    public int hashCode() {
        return Objects.hash(width, height, depth, unit);
    }

    @Override
    public String toString() {
        return "Dimensions{" +
                "width=" + width +
                ", height=" + height +
                ", depth=" + depth +
                ", unit='" + unit + '\'' +
                '}';
    }

    public Builder toBuilder() {
        return new Builder()
                .width(this.width)
                .height(this.height)
                .depth(this.depth)
                .unit(this.unit);
    }

    public static class Builder {
        private Double width;
        private Double height;
        private Double depth;
        private String unit;

        public Builder width(Double width) {
            this.width = width;
            return this;
        }

        public Builder height(Double height) {
            this.height = height;
            return this;
        }

        public Builder depth(Double depth) {
            this.depth = depth;
            return this;
        }

        public Builder unit(String unit) {
            this.unit = unit;
            return this;
        }

        public Dimensions build() {
            if (width == null || !Double.isFinite(width) || width < 0) {
                throw new IllegalArgumentException("width must be finite and non-negative");
            }
            if (height == null || !Double.isFinite(height) || height < 0) {
                throw new IllegalArgumentException("height must be finite and non-negative");
            }
            if (depth == null || !Double.isFinite(depth) || depth < 0) {
                throw new IllegalArgumentException("depth must be finite and non-negative");
            }
            if (unit != null && ContractText.isBlank(unit)) {
                throw new IllegalArgumentException("unit must not be blank if provided");
            }
            return new Dimensions(this);
        }
    }
}



