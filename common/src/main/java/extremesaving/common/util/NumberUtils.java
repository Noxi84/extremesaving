package extremesaving.common.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public final class NumberUtils {

    private NumberUtils() {
    }

    public static boolean isExpense(BigDecimal val) {
        return val != null && BigDecimal.ZERO.compareTo(val) > 0;
    }

    public static boolean isIncome(BigDecimal val) {
        return val != null && BigDecimal.ZERO.compareTo(val) < 0;
    }

    public static boolean isNumber(final String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public static String formatNumber(BigDecimal val) {
        return formatNumber(val, true);
    }

    public static String formatNumber(BigDecimal val, boolean decimals) {
        if (decimals) {
            DecimalFormat df = new DecimalFormat("#,###.00");
            return df.format(val);
        }
        return String.valueOf(val.intValue());
    }

    public static String formatPercentage(BigDecimal val) {
        return val.intValue() + "%";
    }
}