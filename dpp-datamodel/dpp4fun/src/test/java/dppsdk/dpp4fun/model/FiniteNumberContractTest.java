package dppsdk.dpp4fun.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FiniteNumberContractTest {

    @Test
    void characteristicsWeightMustBeFiniteAndNonNegative() {
        assertDoesNotThrow(
                () -> new Characteristics.Builder().productName("Chair").weight(0.0).build());
        assertDoesNotThrow(
                () -> new Characteristics.Builder().productName("Chair").weight(1.25e2).build());
        for (double invalid : new double[] {
                -1.0, Double.NaN, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY}) {
            assertThrows(
                    IllegalArgumentException.class,
                    () -> new Characteristics.Builder()
                            .productName("Chair")
                            .weight(invalid)
                            .build());
        }
    }

    @Test
    void everyDimensionMustBeFiniteAndNonNegative() {
        for (double invalid : new double[] {
                -1.0, Double.NaN, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY}) {
            assertThrows(
                    IllegalArgumentException.class,
                    () -> new Dimensions.Builder()
                            .width(invalid)
                            .height(1.0)
                            .depth(1.0)
                            .unit("cm")
                            .build());
        }
    }

    @Test
    void materialPortionMustBeFiniteAndNonNegative() {
        for (double invalid : new double[] {
                -1.0, Double.NaN, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY}) {
            assertThrows(
                    IllegalArgumentException.class,
                    () -> new Material.Builder().name("Steel").portion(invalid).build());
        }
    }
}
