package extremesaving.util;

import extremesaving.constant.ExtremeSavingConstants;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public final class DateUtils {

    private DateUtils() {
    }

    public static long daysBetween(Date d1, Date d2) {
        return TimeUnit.DAYS.convert(d1.getTime() - d2.getTime(), TimeUnit.MILLISECONDS);
    }

    public static long monthsBetween(Date d1, Date d2) {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(d1);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(d2);

        LocalDate bday = LocalDate.of(cal1.get(Calendar.YEAR), cal1.get(Calendar.MONTH), 1);
        LocalDate today = LocalDate.now();

        Period age = Period.between(bday, today);
        return age.getMonths();
    }

    public static String formatDate(Date date) {
        return new SimpleDateFormat(ExtremeSavingConstants.DATA_CSV_DATE_FORMAT).format(date);
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

    public static boolean equalYears(Date d1, Date d2) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(d1);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(d2);
        return cal.get(Calendar.YEAR) == cal2.get(Calendar.YEAR);
    }
}
