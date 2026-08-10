package dppsdk.core.util;

import java.util.Locale;

/**
 * Stable cross-language text predicates used by contracted model and validation boundaries.
 */
public final class ContractText {

    private ContractText() {
        // Utility class - no instances.
    }

    public static boolean isBlank(String value) {
        if (value == null || value.isEmpty()) {
            return true;
        }
        for (int offset = 0; offset < value.length(); ) {
            int codePoint = value.codePointAt(offset);
            if (!isContractWhitespace(codePoint)) {
                return false;
            }
            offset += Character.charCount(codePoint);
        }
        return true;
    }

    public static String strip(String value) {
        int start = 0;
        int end = value.length();
        while (start < end) {
            int codePoint = value.codePointAt(start);
            if (!isContractWhitespace(codePoint)) {
                break;
            }
            start += Character.charCount(codePoint);
        }
        while (end > start) {
            int codePoint = value.codePointBefore(end);
            if (!isContractWhitespace(codePoint)) {
                break;
            }
            end -= Character.charCount(codePoint);
        }
        return value.substring(start, end);
    }

    public static String normalizeForComparison(String value) {
        return strip(value).toLowerCase(Locale.ROOT);
    }

    private static boolean isContractWhitespace(int codePoint) {
        return (codePoint >= 0x0009 && codePoint <= 0x000d)
                || codePoint == 0x0020
                || codePoint == 0x0085
                || codePoint == 0x00a0
                || codePoint == 0x1680
                || (codePoint >= 0x2000 && codePoint <= 0x200a)
                || codePoint == 0x2028
                || codePoint == 0x2029
                || codePoint == 0x202f
                || codePoint == 0x205f
                || codePoint == 0x3000;
    }
}
