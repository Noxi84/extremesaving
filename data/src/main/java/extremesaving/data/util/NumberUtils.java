package extremesaving.data.util;

import java.math.BigDecimal;

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
}