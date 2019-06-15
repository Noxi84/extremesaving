package extremesaving.sampledata;

import extremesaving.constant.ExtremeSavingConstants;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DefaultSampleDataGenerator implements SampleDataGenerator {

    private static final String START_DATE = "5/6/2014";

    @Override
    public void generateData() throws ParseException {
        Date startDate = new SimpleDateFormat(ExtremeSavingConstants.DATA_CSV_DATE_FORMAT).parse(START_DATE);
        final Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTime(startDate);
    }
}