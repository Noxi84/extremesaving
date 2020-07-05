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

    public static String formatPercentage(BigDecimal val) {
        return val.intValue() + "%";
    }
}