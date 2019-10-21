package extremesaving.charts.builder;

import extremesaving.calculation.dto.MiniResultDto;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import java.util.Calendar;
import java.util.Map;

public class YearlyBarChart {

    private Map<Integer, MiniResultDto> yearResults;

    public YearlyBarChart withYearResults(Map<Integer, MiniResultDto> yearResults) {
        this.yearResults = yearResults;
        return this;
    }

    public JFreeChart build() {
        return ChartFactory.createBarChart("", "", "", createDataset(), PlotOrientation.VERTICAL, false, false, false);
    }

    protected CategoryDataset createDataset() {
        final String result = "Result";

        Calendar cal = Calendar.getInstance();
        int currentYear = cal.get(Calendar.YEAR);

        final String year1 = String.valueOf(currentYear);
        final String year2 = String.valueOf(currentYear - 1);
        final String year3 = String.valueOf(currentYear - 2);
        final String year4 = String.valueOf(currentYear - 3);
        final String year5 = String.valueOf(currentYear - 4);
        final String year6 = String.valueOf(currentYear - 5);
        final String year7 = String.valueOf(currentYear - 6);
        final String year8 = String.valueOf(currentYear - 7);
        final String year9 = String.valueOf(currentYear - 8);
        final DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        MiniResultDto year9Results = yearResults.get(Integer.valueOf(year9));
        MiniResultDto year8Results = yearResults.get(Integer.valueOf(year8));
        MiniResultDto year7Results = yearResults.get(Integer.valueOf(year7));
        MiniResultDto year6Results = yearResults.get(Integer.valueOf(year6));
        MiniResultDto year5Results = yearResults.get(Integer.valueOf(year5));
        MiniResultDto year4Results = yearResults.get(Integer.valueOf(year4));
        MiniResultDto year3Results = yearResults.get(Integer.valueOf(year3));
        MiniResultDto year2Results = yearResults.get(Integer.valueOf(year2));
        MiniResultDto year1Results = yearResults.get(Integer.valueOf(year1));

        dataset.addValue(year9Results.getResult(), result, year9);
        dataset.addValue(year8Results.getResult(), result, year8);
        dataset.addValue(year7Results.getResult(), result, year7);
        dataset.addValue(year6Results.getResult(), result, year6);
        dataset.addValue(year5Results.getResult(), result, year5);
        dataset.addValue(year4Results.getResult(), result, year4);
        dataset.addValue(year3Results.getResult(), result, year3);
        dataset.addValue(year2Results.getResult(), result, year2);
        dataset.addValue(year1Results.getResult(), result, year1);

        return dataset;
    }
}