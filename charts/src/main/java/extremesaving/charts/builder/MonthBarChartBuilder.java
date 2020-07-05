package extremesaving.charts.builder;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import extremesaving.data.dto.MiniResultDto;

/**
 * Builder for the bar chart.
 */
public class MonthBarChartBuilder {

    private Map<Date, MiniResultDto> monthResults;

    /**
     * Add the month results to the chart.
     *
     * @param monthResults Map<Date, MiniResultDto> monthResults containg the end result for each month.
     * @return MonthBarChartBuilder
     */
    public MonthBarChartBuilder withMonthResults(Map<Date, MiniResultDto> monthResults) {
        this.monthResults = monthResults;
        return this;
    }

    /**
     * Build the JFreeChart object.
     *
     * @return JFreeChart
     */
    public JFreeChart build() {
        return ChartFactory.createBarChart("", "", "", createDataset(), PlotOrientation.VERTICAL, false, false, false);
    }

    protected CategoryDataset createDataset() {
        final String incomes = "Incomes";
        final String result = "Result";
        final String expenses = "Expenses";

        final DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        List<Date> sortedDates = monthResults.entrySet().stream().map(entry -> entry.getKey()).sorted(Date::compareTo).collect(Collectors.toList());

        List<String> titles = new ArrayList<>();
        final SimpleDateFormat sf = new SimpleDateFormat("MMM yyyy");
        sortedDates.forEach(date -> titles.add(sf.format(date)));

        MiniResultDto januaryResults = monthResults.get(sortedDates.get(0));
        MiniResultDto februaryResults = monthResults.get(sortedDates.get(1));
        MiniResultDto marchResults = monthResults.get(sortedDates.get(2));
        MiniResultDto aprilResults = monthResults.get(sortedDates.get(3));
        MiniResultDto mayResults = monthResults.get(sortedDates.get(4));
        MiniResultDto juneResults = monthResults.get(sortedDates.get(5));
        MiniResultDto julyResults = monthResults.get(sortedDates.get(6));
        MiniResultDto augustResults = monthResults.get(sortedDates.get(7));
        MiniResultDto septemberResults = monthResults.get(sortedDates.get(8));
        MiniResultDto octoberResults = monthResults.get(sortedDates.get(9));
        MiniResultDto novemberResults = monthResults.get(sortedDates.get(10));
        MiniResultDto decemberResults = monthResults.get(sortedDates.get(11));

        dataset.addValue(januaryResults.getExpenses().multiply(BigDecimal.valueOf(-1)), expenses, titles.get(0));
        dataset.addValue(februaryResults.getExpenses().multiply(BigDecimal.valueOf(-1)), expenses, titles.get(1));
        dataset.addValue(marchResults.getExpenses().multiply(BigDecimal.valueOf(-1)), expenses, titles.get(2));
        dataset.addValue(aprilResults.getExpenses().multiply(BigDecimal.valueOf(-1)), expenses, titles.get(3));
        dataset.addValue(mayResults.getExpenses().multiply(BigDecimal.valueOf(-1)), expenses, titles.get(4));
        dataset.addValue(juneResults.getExpenses().multiply(BigDecimal.valueOf(-1)), expenses, titles.get(5));
        dataset.addValue(julyResults.getExpenses().multiply(BigDecimal.valueOf(-1)), expenses, titles.get(6));
        dataset.addValue(augustResults.getExpenses().multiply(BigDecimal.valueOf(-1)), expenses, titles.get(7));
        dataset.addValue(septemberResults.getExpenses().multiply(BigDecimal.valueOf(-1)), expenses, titles.get(8));
        dataset.addValue(octoberResults.getExpenses().multiply(BigDecimal.valueOf(-1)), expenses, titles.get(9));
        dataset.addValue(novemberResults.getExpenses().multiply(BigDecimal.valueOf(-1)), expenses, titles.get(10));
        dataset.addValue(decemberResults.getExpenses().multiply(BigDecimal.valueOf(-1)), expenses, titles.get(11));

        dataset.addValue(januaryResults.getResult(), result, titles.get(0));
        dataset.addValue(februaryResults.getResult(), result, titles.get(1));
        dataset.addValue(marchResults.getResult(), result, titles.get(2));
        dataset.addValue(aprilResults.getResult(), result, titles.get(3));
        dataset.addValue(mayResults.getResult(), result, titles.get(4));
        dataset.addValue(juneResults.getResult(), result, titles.get(5));
        dataset.addValue(julyResults.getResult(), result, titles.get(6));
        dataset.addValue(augustResults.getResult(), result, titles.get(7));
        dataset.addValue(septemberResults.getResult(), result, titles.get(8));
        dataset.addValue(octoberResults.getResult(), result, titles.get(9));
        dataset.addValue(novemberResults.getResult(), result, titles.get(10));
        dataset.addValue(decemberResults.getResult(), result, titles.get(11));

        dataset.addValue(januaryResults.getIncomes(), incomes, titles.get(0));
        dataset.addValue(februaryResults.getIncomes(), incomes, titles.get(1));
        dataset.addValue(marchResults.getIncomes(), incomes, titles.get(2));
        dataset.addValue(aprilResults.getIncomes(), incomes, titles.get(3));
        dataset.addValue(mayResults.getIncomes(), incomes, titles.get(4));
        dataset.addValue(juneResults.getIncomes(), incomes, titles.get(5));
        dataset.addValue(julyResults.getIncomes(), incomes, titles.get(6));
        dataset.addValue(augustResults.getIncomes(), incomes, titles.get(7));
        dataset.addValue(septemberResults.getIncomes(), incomes, titles.get(8));
        dataset.addValue(octoberResults.getIncomes(), incomes, titles.get(9));
        dataset.addValue(novemberResults.getIncomes(), incomes, titles.get(10));
        dataset.addValue(decemberResults.getIncomes(), incomes, titles.get(11));

        return dataset;
    }
}