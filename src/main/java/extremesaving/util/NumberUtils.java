package extremesaving.util;

import java.math.BigDecimal;

public final class NumberUtils {

    private NumberUtils() {
    }


    public static String roundOffTo2DecPlaces(BigDecimal val) {
        return String.format("%.2f", val);
    }


}
