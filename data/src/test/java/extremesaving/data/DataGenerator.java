package extremesaving.data;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import org.junit.Test;

public class DataGenerator {

    private List<SampleCategory> categories = Arrays.asList(
            new SampleCategory("Work", false, true, 1800, 2000, 12),
            new SampleCategory("Taxes", true, false, 100, 500, 3),
            new SampleCategory("Friends & Family", true, true, 2, 100, 20),
            new SampleCategory("Garbage & Recycling", true, false, 15, 20, 4),
            new SampleCategory("Rent", true, false, 500, 600, 12),
            new SampleCategory("Electronics & Toys", true, false, 2, 500, 6),
            new SampleCategory("Food & Drinks", true, false, 20, 25, 200),
            new SampleCategory("Car & Gas", true, false, 25, 50, 30),
            new SampleCategory("Entertainment", true, false, 2, 50, 20),
            new SampleCategory("Internet/Phone/Cable", true, false, 60, 70, 12),
            new SampleCategory("Electricity & Water", true, false, 2, 100, 12)
    );

    @Test
    public void generate() {
        Calendar startDate = Calendar.getInstance();
        startDate.set(2010, Calendar.JANUARY, 1);
        Calendar endDate = Calendar.getInstance();

        int numberOfYears = endDate.get(Calendar.YEAR) - startDate.get(Calendar.YEAR);

        List<String> csvLines = new ArrayList<>();
        csvLines.add("#date,category,value");

        for (int startYear = 0; startYear < numberOfYears; startYear++) {
            for (SampleCategory sampleCategory : categories) {

                Calendar randomDateInYear = Calendar.getInstance();
                randomDateInYear.add(Calendar.YEAR, startYear * -1);

                for (int i = 0; i < sampleCategory.getOccurrencesPerYear(); i++) {
                    randomDateInYear.set(Calendar.DAY_OF_MONTH, (int) getRandom(0, 27));
                    randomDateInYear.set(Calendar.MONTH, (int) getRandom(Calendar.JANUARY, Calendar.DECEMBER));

                    String value = "";
                    if (sampleCategory.isIncome() && sampleCategory.isExpense()) {
                        if ((int) getRandom(0, 1) == 1) {
                            value += "-";
                        }
                    } else if (sampleCategory.isExpense()) {
                        value += "-";
                    }

                    value += (int) getRandom(sampleCategory.getMinAmount(), sampleCategory.getMaxAmount()) + "." + (int) getRandom(0, 99);

                    String csvLine = new SimpleDateFormat("dd/MM/yyyy").format(randomDateInYear.getTime()) + "," + sampleCategory.getName() + "," + value;
                    csvLines.add(csvLine);
                }
            }
        }

        for (String line : csvLines) {
            System.out.println(line);
        }
    }

    private double getRandom(int min, int max) {
        return Math.random() * (max - min + 1) + min;
    }
}