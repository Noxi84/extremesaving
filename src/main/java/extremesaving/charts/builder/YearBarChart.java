package extremesaving.charts.builder;

import extremesaving.calculation.dto.MiniResultDto;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Map;

public class YearBarChart {

    private Map<Integer, MiniResultDto> yearResults;

    public YearBarChart withYearResults(Map<Integer, MiniResultDto> yearResults) {
        this.yearResults = yearResults;
        return this;
    }

    public JFreeChart build() {
        return ChartFactory.createBarChart("", "", "", createDataset(), PlotOrientation.VERTICAL, false, false, false);
    }

    protected CategoryDataset createDataset() {
        final String expenses = "Expenses";
        final String result = "Result";
        final String incomes = "Incomes";

        Integer lastYear = yearResults.entrySet().stream().max(Comparator.comparing(Map.Entry::getKey)).get().getKey();

        final String year1 = String.valueOf(lastYear);
        final String year2 = String.valueOf(lastYear - 1);
        final String year3 = String.valueOf(lastYear - 2);
        final String year4 = String.valueOf(lastYear - 3);
        final String year5 = String.valueOf(lastYear - 4);
        final String year6 = String.valueOf(lastYear - 5);
        final String year7 = String.valueOf(lastYear - 6);
        final String year8 = String.valueOf(lastYear - 7);
        final String year9 = String.valueOf(lastYear - 8);
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

        dataset.addValue(year9Results.getExpenses().multiply(BigDecimal.valueOf(-1)), expenses, year9);
        dataset.addValue(year8Results.getExpenses().multiply(BigDecimal.valueOf(-1)), expenses, year8);
        dataset.addValue(year7Results.getExpenses().multiply(BigDecimal.valueOf(-1)), expenses, year7);
        dataset.addValue(year6Results.getExpenses().multiply(BigDecimal.valueOf(-1)), expenses, year6);
        dataset.addValue(year5Results.getExpenses().multiply(BigDecimal.valueOf(-1)), expenses, year5);
        dataset.addValue(year4Results.getExpenses().multiply(BigDecimal.valueOf(-1)), expenses, year4);
        dataset.addValue(year3Results.getExpenses().multiply(BigDecimal.valueOf(-1)), expenses, year3);
        dataset.addValue(year2Results.getExpenses().multiply(BigDecimal.valueOf(-1)), expenses, year2);
        dataset.addValue(year1Results.getExpenses().multiply(BigDecimal.valueOf(-1)), expenses, year1);

        dataset.addValue(year9Results.getResult(), result, year9);
        dataset.addValue(year8Results.getResult(), result, year8);
        dataset.addValue(year7Results.getResult(), result, year7);
        dataset.addValue(year6Results.getResult(), result, year6);
        dataset.addValue(year5Results.getResult(), result, year5);
        dataset.addValue(year4Results.getResult(), result, year4);
        dataset.addValue(year3Results.getResult(), result, year3);
        dataset.addValue(year2Results.getResult(), result, year2);
        dataset.addValue(year1Results.getResult(), result, year1);

        dataset.addValue(year9Results.getIncomes(), incomes, year9);
        dataset.addValue(year8Results.getIncomes(), incomes, year8);
        dataset.addValue(year7Results.getIncomes(), incomes, year7);
        dataset.addValue(year6Results.getIncomes(), incomes, year6);
        dataset.addValue(year5Results.getIncomes(), incomes, year5);
        dataset.addValue(year4Results.getIncomes(), incomes, year4);
        dataset.addValue(year3Results.getIncomes(), incomes, year3);
        dataset.addValue(year2Results.getIncomes(), incomes, year2);
        dataset.addValue(year1Results.getIncomes(), incomes, year1);

        return dataset;
    }
}