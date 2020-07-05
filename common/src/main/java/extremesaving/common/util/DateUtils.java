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

    /**
     * Get a sorted list of dates for the last 12 months before the given date.
     *
     * @param date Date to determine last 12 months.
     * @return sorted list for last 12 months before the date.
     */
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

    /**
     * Get a Date object for the given day, month and year. Time will be set to 0.
     *
     * @param calendarDay Day of the month (1-31)
     * @param calendarMonth Calendar month number. (0 = January, 11 = December)
     * @param calendarYear Year
     * @return Date object
     */
    public static Date getDate(int calendarDay, int calendarMonth, int calendarYear) {
        Calendar cal = Calendar.getInstance();
        cal.set(calendarYear, calendarMonth, calendarDay, 0, 0, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    /**
     * Get the number of days between two given dates.
     *
     * @param d1 Date
     * @param d2 Date
     * @return number of days between two given dates
     */
    public static long getDaysBetween(Date d1, Date d2) {
        return TimeUnit.DAYS.convert(d1.getTime() - d2.getTime(), TimeUnit.MILLISECONDS);
    }

    /**
     * Check if two dates have the same day, month and year. Time will not be checked.
     *
     * @param d1 Date
     * @param d2 Date
     * @return true or false depending the two dates have the same day, month and year.
     */
    public static boolean isEqualDates(Date d1, Date d2) {
        SimpleDateFormat sf = new SimpleDateFormat("DD/MM/YYYY");
        return sf.format(d1).equals(sf.format(d2));
    }

    /**
     * Check if all dates have the same month and year.
     *
     * @param months List of months to check
     * @param date Date containing month and year to be matched.
     * @return true or false depending if the months contain the same month and year.
     */
    public static boolean isEqualYearAndMonth(List<Date> months, Date date) {
        for (Date month : months) {
            if (DateUtils.isEqualYearAndMonth(date, month)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Check if all dates have the same month.
     *
     * @param d1 List of months to check
     * @param d2 Date containing month to be matched.
     * @return true or false depending if the dates have the same month.
     */
    public static boolean isEqualMonth(Date d1, Date d2) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(d1);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(d2);
        return cal.get(Calendar.MONTH) == cal2.get(Calendar.MONTH);
    }

    /**
     * Check if the dates have the same month and year.
     *
     * @param d1 date with month to check
     * @param d2 Date containing month to be matched.
     * @return true or false depending if the dates have the same month and year.
     */
    public static boolean isEqualYearAndMonth(Date d1, Date d2) {
        return isEqualMonth(d1, d2) && isEqualYear(d1, d2);
    }

    /**
     * Check if all dates have the same year.
     *
     * @param d1 Date containing year to be checked.
     * @param d2 Date containing month to be matched.
     * @return true or false depending if the dates have the same year.
     */
    public static boolean isEqualYear(Date d1, Date d2) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(d1);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(d2);
        return cal.get(Calendar.YEAR) == cal2.get(Calendar.YEAR);
    }
}