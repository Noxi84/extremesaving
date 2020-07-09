package extremesaving.data;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.junit.Test;

public class DataGenerator {

    @Test
    public void generate() {
        Calendar startDate = Calendar.getInstance();
        startDate.set(2015, Calendar.JANUARY, 1);
        Calendar endDate = Calendar.getInstance();

        List<String> csvLines = new ArrayList<>();
        csvLines.add("#date,category,value");

        List<String> categories = new ArrayList<>();
        categories.add("Work");
        categories.add("Taxes");
        categories.add("Friends & Family");
        categories.add("Garbage & Recycling");
        categories.add("Rent");
        categories.add("Electronics & Toys");
        categories.add("Food & Drinks");
        categories.add("Entertainment");
        categories.add("Medic & Insurances");
        categories.add("Internet/Phone/Cable");
        categories.add("Electricity");

        while (startDate.before(endDate)) {

            int categoriesToAdd = (int) getRandom(1, categories.size() - 1);
            for (int i = 0; i < categoriesToAdd; i++) {
                int categoryIndex = (int) getRandom(0, categories.size() - 1);
                String category = categories.get(categoryIndex);
                boolean income = getRandom(0, 100) > 90;
                String value;
                if (income) {
                    value = (int) getRandom(1800, 2500) + "." + (int) getRandom(0, 99);
                } else {
                    value = "-" + (int) getRandom(0, 50) + "." + (int) getRandom(0, 99);
                }
                String csvLine = new SimpleDateFormat("dd/MM/yyyy").format(startDate.getTime()) + "," + category + "," + value;
                csvLines.add(csvLine);
            }

            int daysToAdd = (int) getRandom(0, 10);
            startDate.add(Calendar.DAY_OF_MONTH, daysToAdd);
        }

        for (String line : csvLines) {
            System.out.println(line);
        }
    }

    private double getRandom(int min, int max) {
        return Math.random() * (max - min + 1) + min;
    }
}