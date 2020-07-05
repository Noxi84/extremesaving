package extremesaving.common.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Utility class for handling dates.
 */
public final class DateUtils {

    private DateUtils() {
    }

    public static List<Date> getLastMonths(Date date) {
        Calendar lastDate = Calendar.getInstance();
        lastDate.setTime(date);

        List<Date> lastMonths = new ArrayList<>();

        lastMonths.add(DateUtils.getDate(1, lastDate.get(Calendar.MONTH), lastDate.get(Calendar.YEAR)));
        lastDate.add(Calendar.MONTH, -1);

        lastMonths.add(DateUtils.getDate(1, lastDate.get(Calendar.MONTH), lastDate.get(Calendar.YEAR)));
        lastDate.add(Calendar.MONTH, -1);

        lastMonths.add(DateUtils.getDate(1, lastDate.get(Calendar.MONTH), lastDate.get(Calendar.YEAR)));
        lastDate.add(Calendar.MONTH, -1);

        lastMonths.add(DateUtils.getDate(1, lastDate.get(Calendar.MONTH), lastDate.get(Calendar.YEAR)));
        lastDate.add(Calendar.MONTH, -1);

        lastMonths.add(DateUtils.getDate(1, lastDate.get(Calendar.MONTH), lastDate.get(Calendar.YEAR)));
        lastDate.add(Calendar.MONTH, -1);

        lastMonths.add(DateUtils.getDate(1, lastDate.get(Calendar.MONTH), lastDate.get(Calendar.YEAR)));
        lastDate.add(Calendar.MONTH, -1);

        lastMonths.add(DateUtils.getDate(1, lastDate.get(Calendar.MONTH), lastDate.get(Calendar.YEAR)));
        lastDate.add(Calendar.MONTH, -1);

        lastMonths.add(DateUtils.getDate(1, lastDate.get(Calendar.MONTH), lastDate.get(Calendar.YEAR)));
        lastDate.add(Calendar.MONTH, -1);

        lastMonths.add(DateUtils.getDate(1, lastDate.get(Calendar.MONTH), lastDate.get(Calendar.YEAR)));
        lastDate.add(Calendar.MONTH, -1);

        lastMonths.add(DateUtils.getDate(1, lastDate.get(Calendar.MONTH), lastDate.get(Calendar.YEAR)));
        lastDate.add(Calendar.MONTH, -1);

        lastMonths.add(DateUtils.getDate(1, lastDate.get(Calendar.MONTH), lastDate.get(Calendar.YEAR)));
        lastDate.add(Calendar.MONTH, -1);

        lastMonths.add(DateUtils.getDate(1, lastDate.get(Calendar.MONTH), lastDate.get(Calendar.YEAR)));

        return lastMonths;
    }

    public static boolean isEqualYearAndMonth(List<Date> lastMonths, Date date) {
        for (Date month : lastMonths) {
            if (DateUtils.isEqualYearAndMonth(date, month)) {
                return true;
            }
        }
        return false;
    }

    public static Date getDate(int calendarDay, int calendarMonth, int calendarYear) {
        Calendar cal = Calendar.getInstance();
        cal.set(calendarYear, calendarMonth, calendarDay, 0, 0, 0);
        return cal.getTime();
    }

    public static long getDaysBetween(Date d1, Date d2) {
        return TimeUnit.DAYS.convert(d1.getTime() - d2.getTime(), TimeUnit.MILLISECONDS);
    }

    public static boolean isEqualDates(Date d1, Date d2) {
        SimpleDateFormat sf = new SimpleDateFormat("DD/MM/YYYY");
        return sf.format(d1).equals(sf.format(d2));
    }

    public static boolean isEqualMonth(Date d1, Date d2) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(d1);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(d2);
        return cal.get(Calendar.MONTH) == cal2.get(Calendar.MONTH);
    }

    public static boolean isEqualYearAndMonth(Date d1, Date d2) {
        return isEqualMonth(d1, d2) && isEqualYear(d1, d2);
    }

    public static boolean isEqualYear(Date d1, Date d2) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(d1);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(d2);
        return cal.get(Calendar.YEAR) == cal2.get(Calendar.YEAR);
    }
}