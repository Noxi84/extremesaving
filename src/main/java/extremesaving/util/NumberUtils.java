package extremesaving.util;

import java.math.BigDecimal;

public final class NumberUtils {

    private NumberUtils() {
    }

    public static String formatNumber(BigDecimal val, boolean useSymbol) {
        if (useSymbol) {
            return "€ " + roundOffTo2DecPlaces(val);
        }
        return roundOffTo2DecPlaces(val) + " EUR";
    }


    public static String roundOffTo2DecPlaces(BigDecimal val) {
        return String.format("%.2f", val);
    }


}
