package extremesaving.util;

import java.math.BigDecimal;
import java.util.Random;

public final class NumberUtils {

    private NumberUtils() {
    }

    public static String formatNumber(BigDecimal val) {
        return formatNumber(val, true);
    }

    public static String formatNumber(BigDecimal val, boolean decimals) {
        if (decimals) {
            return "€ " + roundOffTo2DecPlaces(val);
        }
        return "€ " + val.intValue();
    }

    public static String roundOffTo2DecPlaces(BigDecimal val) {
        return String.format("%.2f", val);
    }

    public static String formatPercentage(BigDecimal val) {
        return val.intValue() + "%";
    }

    public static int getRandom(int min, int max) {
        return new Random().nextInt(max - min + 1) + min;
    }
}