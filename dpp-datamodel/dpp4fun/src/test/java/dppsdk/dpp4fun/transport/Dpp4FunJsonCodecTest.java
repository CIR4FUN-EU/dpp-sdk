package dppsdk.dpp4fun.transport;

import dppsdk.dpp4fun.model.Dpp4Fun;
import dppsdk.core.mapper.MappingException;
import dppsdk.support.TestDataFactory;
import dppsdk.core.validation.ValidationException;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class Dpp4FunJsonCodecTest {

    private final Dpp4FunJsonCodec codec = new Dpp4FunJsonCodec();

    @Test
    void toJsonProducesExpectedOutboundFields() {
        Dpp4Fun domain = TestDataFactory.validDpp();

        String json = codec.toJson(domain);

        assertTrue(json.contains("\"passportMetadata\""));
        assertTrue(json.contains("\"classification\""));
        assertTrue(json.contains("\"characteristics\""));
        assertTrue(json.contains("\"nameplate\""));
        assertTrue(json.contains("\"billOfMaterials\""));
        assertTrue(json.contains("\"documentation\""));
        assertTrue(json.contains(domain.getUniqueProductIdentifier().toString()));
        assertTrue(json.contains("\"role\":\"MANUFACTURER\""));
    }

    @Test
    void domainJsonDomainRoundTripPreservesObjectGraph() {
        Dpp4Fun domain = TestDataFactory.validDpp();

        String json = codec.toJson(domain);
        Dpp4Fun roundTripped = codec.fromJson(json);

        assertEquals(domain, roundTripped);
        assertEquals(domain.getDppId(), roundTripped.getDppId());
        assertEquals(domain.getProductId(), roundTripped.getProductId());
    }

    @Test
    void fromJsonAndValidateAcceptsValidJson() {
        Dpp4Fun domain = TestDataFactory.validDpp();

        Dpp4Fun parsed = codec.fromJsonAndValidate(codec.toJson(domain));

        assertEquals(domain, parsed);
    }

    @Test
    void fromJsonRejectsMalformedJson() {
        assertThrows(IllegalArgumentException.class, () -> codec.fromJson("{not-json"));
    }

    @Test
    void fromJsonRejectsNullAndMissingRootsAsCausalMappingFailures() {
        for (String invalidRoot : new String[] {"null", "", "[]"}) {
            IllegalArgumentException error =
                    assertThrows(IllegalArgumentException.class, () -> codec.fromJson(invalidRoot));
            assertTrue(error.getMessage().contains("deserialize"));
            assertTrue(error.getCause() instanceof IllegalArgumentException);
            assertTrue(error.getCause().getMessage().contains("$"));
        }
    }

    @Test
    void fromJsonRejectsEmptyObjectAsCausalStructuralMappingFailure() {
        MappingException error =
                assertThrows(MappingException.class, () -> codec.fromJson("{}"));
        assertTrue(error.getCause() instanceof IllegalArgumentException);
    }

    @Test
    void fromJsonRejectsInvalidUuidDateRoleAndMissingRequiredData() {
        Dpp4Fun domain = TestDataFactory.validDpp();
        String validJson = codec.toJson(domain);

        String invalidUuidJson = validJson
                .replace(domain.getUniqueProductIdentifier().toString(), "not-a-uuid");

        String invalidDateJson = validJson
                .replace(domain.getPassportUpdateDates().get(0).toString(), "2026-13-01");

        String invalidRoleJson = validJson
                .replace("\"MANUFACTURER\"", "\"BROKER\"");

        String missingNameplateJson = """
                {
                  "passportMetadata": {
                    "uniqueProductIdentifier": "%s",
                    "passportUpdateDates": ["2026-04-23"]
                  },
                  "classification": {
                    "sector": "Furniture",
                    "group": "Seating",
                    "category": "Beds",
                    "tags": ["demo"]
                  },
                  "characteristics": {
                    "productName": "Demo Bed",
                    "productType": "Bed"
                  }
                }
                """.formatted(domain.getUniqueProductIdentifier());

        assertThrows(MappingException.class, () -> codec.fromJson(invalidUuidJson));
        assertThrows(MappingException.class, () -> codec.fromJson(invalidDateJson));
        assertThrows(MappingException.class, () -> codec.fromJson(invalidRoleJson));
        assertThrows(MappingException.class, () -> codec.fromJson(missingNameplateJson));
    }

    @Test
    void fromJsonAndValidateRejectsSemanticallyInvalidInboundData() {
        String invalidSemanticJson = TestDataFactory.validDppJson()
                .replace("\"productType\":\"Bed\"", "\"productType\":\"Chair\"");

        assertThrows(ValidationException.class, () -> codec.fromJsonAndValidate(invalidSemanticJson));
    }

    @Test
    void fromJsonRejectsNullFeatureAsCausalIndexedMappingFailure() {
        String invalidJson = TestDataFactory.validDppJson()
                .replace("\"features\":[\"Feature A\",\"Feature B\"]",
                        "\"features\":[null,\"Feature B\"]");

        MappingException error =
                assertThrows(MappingException.class, () -> codec.fromJson(invalidJson));

        assertTrue(error.getMessage().contains("Characteristics.features[0]"));
        assertTrue(error.getCause() instanceof IllegalArgumentException);
    }

    @Test
    void fromJsonRejectsLaterNullTagAsCausalIndexedMappingFailure() {
        String invalidJson = TestDataFactory.validDppJson()
                .replace("\"tags\":[\"demo\"]", "\"tags\":[\"demo\",null]");

        MappingException error =
                assertThrows(MappingException.class, () -> codec.fromJsonAndValidate(invalidJson));

        assertTrue(error.getMessage().contains("ProductClassification.tags[1]"));
        assertTrue(error.getCause() instanceof IllegalArgumentException);
    }

    @Test
    void fromJsonAcceptsFiniteExponentAndRejectsNonFiniteNumbers() {
        String validJson = TestDataFactory.validDppJson();
        assertEquals(
                75.0,
                codec.fromJson(validJson.replace("\"weight\":75.0", "\"weight\":7.5e1"))
                        .getWeight());

        for (String invalid : new String[] {"NaN", "Infinity", "-Infinity"}) {
            IllegalArgumentException error = assertThrows(
                    IllegalArgumentException.class,
                    () -> codec.fromJson(
                            validJson.replace("\"weight\":75.0", "\"weight\":" + invalid)));
            assertTrue(error.getCause() instanceof com.fasterxml.jackson.core.JsonProcessingException);
        }
        MappingException overflow = assertThrows(
                MappingException.class,
                () -> codec.fromJson(
                        validJson.replace("\"weight\":75.0", "\"weight\":1e309")));
        assertTrue(overflow.getCause() instanceof IllegalArgumentException);
    }

    @Test
    void toJsonRejectsDefensivelyInjectedNonFiniteNumberWithCause() throws Exception {
        Dpp4Fun dpp = TestDataFactory.validDpp();
        Field weight = dpp.getCharacteristics().getClass().getDeclaredField("weight");
        weight.setAccessible(true);
        weight.set(dpp.getCharacteristics(), Double.POSITIVE_INFINITY);

        IllegalArgumentException error =
                assertThrows(IllegalArgumentException.class, () -> codec.toJson(dpp));

        assertTrue(error.getCause() instanceof IllegalArgumentException);
        assertTrue(error.getCause().getMessage().contains("Characteristics.weight"));
    }

    @Test
    void fromJsonAcceptsNestedCoreDppJson() {
        Dpp4Fun domain = TestDataFactory.validDpp();
        String json = """
                {
                  "coreDpp": {
                    "passportMetadata": {
                      "uniqueProductIdentifier": "%s",
                      "passportUpdateDates": ["%s"],
                      "qrCodeOrDigitalTag": "QR-DEMO-001",
                      "externalDocumentationLink": "https://example.com/doc"
                    },
                    "nameplate": {
                      "gtinCode": "GTIN-DEMO-001",
                      "internalArticleNumber": "ART-001",
                      "batchNumber": "BATCH-2026",
                      "manufacturer": {
                        "name": "Demo Manufacturer GmbH",
                        "gln": "123456789",
                        "uri": "https://manufacturer.example.com",
                        "role": "MANUFACTURER"
                      }
                    },
                    "documentation": {
                      "digitalInstructionsLink": "https://example.com/instructions.pdf",
                      "safetyInstructionsLink": "https://example.com/safety.pdf",
                      "downloadable": true,
                      "availableForYears": 10
                    }
                  },
                  "classification": {
                    "sector": "Furniture",
                    "group": "Seating",
                    "category": "Beds",
                    "tags": ["demo"]
                  },
                  "characteristics": {
                    "productName": "Demo Bed",
                    "description": "A demo product for testing",
                    "brand": "Dpp4Fun",
                    "productType": "Bed",
                    "dimensions": {
                      "width": 200.0,
                      "height": 90.0,
                      "depth": 50.0,
                      "unit": "cm"
                    },
                    "weight": 75.0,
                    "features": ["Feature A", "Feature B"]
                  },
                  "billOfMaterials": {
                    "materials": [
                      {"name": "Steel", "mandatory": true, "portion": 2.5, "reference": "MAT-001"},
                      {"name": "Foam", "mandatory": true, "portion": 5.0, "reference": "MAT-002"}
                    ],
                    "components": [
                      {"name": "Frame Assembly", "reference": "COMP-001"}
                    ],
                    "parts": [
                      {"name": "Mattress Support", "mandatory": true, "reference": "PART-001"}
                    ]
                  }
                }
                """.formatted(
                        domain.getUniqueProductIdentifier(),
                        domain.getPassportUpdateDates().get(0));

        assertEquals(domain, codec.fromJson(json));
    }

    @Test
    void fromJsonPrefersNestedCoreDppWhenBothShapesArePresent() {
        Dpp4Fun domain = TestDataFactory.validDpp();
        String json = """
                {
                  "coreDpp": {
                    "passportMetadata": {
                      "uniqueProductIdentifier": "%s",
                      "passportUpdateDates": ["%s"],
                      "qrCodeOrDigitalTag": "QR-DEMO-001"
                    },
                    "nameplate": {
                      "gtinCode": "GTIN-DEMO-001",
                      "manufacturer": {
                        "name": "Demo Manufacturer GmbH",
                        "role": "MANUFACTURER"
                      }
                    }
                  },
                  "passportMetadata": {
                    "uniqueProductIdentifier": "00000000-0000-0000-0000-000000000000",
                    "passportUpdateDates": ["2026-01-01"]
                  },
                  "nameplate": {
                    "gtinCode": "GTIN-IGNORED-001",
                    "manufacturer": {
                      "name": "Ignored Manufacturer",
                      "role": "MANUFACTURER"
                    }
                  },
                  "classification": {
                    "sector": "Furniture",
                    "group": "Seating",
                    "category": "Beds",
                    "tags": ["demo"]
                  },
                  "characteristics": {
                    "productName": "Demo Bed",
                    "productType": "Bed"
                  }
                }
                """.formatted(
                        domain.getUniqueProductIdentifier(),
                        domain.getPassportUpdateDates().get(0));

        Dpp4Fun parsed = codec.fromJson(json);

        assertEquals(domain.getUniqueProductIdentifier(), parsed.getUniqueProductIdentifier());
        assertEquals(domain.getGtinCode(), parsed.getGtinCode());
    }
}

