package extremesaving.calculation.util;

import java.math.BigDecimal;

public final class NumberUtils {

    private NumberUtils() {
    }

    public static boolean isExpense(BigDecimal val) {
        return BigDecimal.ZERO.compareTo(val) > 0;
    }

    public static boolean isIncome(BigDecimal val) {
        return BigDecimal.ZERO.compareTo(val) < 0;
    }
}