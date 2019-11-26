package extremesaving.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public final class DateUtils {

    private DateUtils() {
    }

    public static long daysBetween(Date d1, Date d2) {
        return TimeUnit.DAYS.convert(d1.getTime() - d2.getTime(), TimeUnit.MILLISECONDS);
    }

    public static boolean equalDates(Date d1, Date d2) {
        SimpleDateFormat sf = new SimpleDateFormat("DD/MM/YYYY");
        return sf.format(d1).equals(sf.format(d2));
    }

    public static boolean equalMonths(Date d1, Date d2) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(d1);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(d2);
        return cal.get(Calendar.MONTH) == cal2.get(Calendar.MONTH);
    }

    public static boolean equalYearAndMonths(Date d1, Date d2) {
        return equalMonths(d1, d2) && equalYears(d1, d2);
    }

    public static boolean equalYears(Date d1, Date d2) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(d1);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(d2);
        return cal.get(Calendar.YEAR) == cal2.get(Calendar.YEAR);
    }
}