package extremesaving.common.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * Utility class for handling numbers.
 */
public final class NumberUtils {

    private NumberUtils() {
    }

    /**
     * Check if the value is negative.
     *
     * @param val value
     * @return true or false.
     */
    public static boolean isExpense(BigDecimal val) {
        return val != null && BigDecimal.ZERO.compareTo(val) > 0;
    }

    /**
     * Check if the value is positive.
     *
     * @param val value
     * @return true or false.
     */
    public static boolean isIncome(BigDecimal val) {
        return val != null && BigDecimal.ZERO.compareTo(val) < 0;
    }

    /**
     * Format the value to a String. For example -12,589.45
     *
     * @param val value
     * @return Formatted value.
     */
    public static String formatNumber(BigDecimal val) {
        DecimalFormat df = new DecimalFormat("#,###.00");
        String result = df.format(val);
        if (result.startsWith(".")) {
            return "0" + result;
        } else if (result.startsWith("-.")) {
            return "-0" + result.substring(1);
        }
        return result;
    }

    /**
     * Format the value to a percentage.
     *
     * @param val value must be between 0 and 100.
     * @return Percentage value.
     */
    public static String formatPercentage(BigDecimal val) {
        if (val.compareTo(BigDecimal.valueOf(0)) < 0 || val.compareTo(BigDecimal.valueOf(100)) > 0) {
            throw new IllegalArgumentException("Percentage value should be between 0 and 100.");
        }
        return val.intValue() + "%";
    }
}