package extremesaving.charts.service;

import extremesaving.calculation.dto.MiniResultDto;
import extremesaving.pdf.service.PdfPageTipOfTheDayService;
import extremesaving.pdf.util.PdfUtils;
import extremesaving.property.PropertiesValueHolder;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Map;

import static extremesaving.property.PropertyValueEnum.MONTHLY_BAR_CHART_IMAGE_FILE;

public class MonthlyBarChartService implements ChartService {

    private ChartDataService chartDataService;

    @Override
    public void generateChartPng() {
        JFreeChart barChart = ChartFactory.createBarChart("", "", "", createDataset(), PlotOrientation.VERTICAL, false, false, false);
        PdfUtils.writeChartPng(barChart, PropertiesValueHolder.getInstance().getPropValue(MONTHLY_BAR_CHART_IMAGE_FILE), (int) PdfPageTipOfTheDayService.MONTHCHART_WIDTH * 2, (int) PdfPageTipOfTheDayService.MONTHCHART_HEIGHT * 2);
    }

    private CategoryDataset createDataset() {
        Map<Integer, MiniResultDto> monthlyResults = chartDataService.getMonthlyResults();

        final String incomes = "Incomes";
        final String result = "Result";
        final String expenses = "Expenses";
        final String january = "Jan";
        final String february = "Feb";
        final String march = "Mar";
        final String april = "Apr";
        final String may = "May";
        final String june = "Jun";
        final String july = "Jul";
        final String august = "Au";
        final String september = "Sep";
        final String october = "Oct";
        final String november = "Nov";
        final String december = "Dec";
        final DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        MiniResultDto januaryResults = monthlyResults.get(Calendar.JANUARY);
        MiniResultDto februaryResults = monthlyResults.get(Calendar.FEBRUARY);
        MiniResultDto marchResults = monthlyResults.get(Calendar.MARCH);
        MiniResultDto aprilResults = monthlyResults.get(Calendar.APRIL);
        MiniResultDto mayResults = monthlyResults.get(Calendar.MAY);
        MiniResultDto juneResults = monthlyResults.get(Calendar.JUNE);
        MiniResultDto julyResults = monthlyResults.get(Calendar.JULY);
        MiniResultDto augustResults = monthlyResults.get(Calendar.AUGUST);
        MiniResultDto septemberResults = monthlyResults.get(Calendar.SEPTEMBER);
        MiniResultDto octoberResults = monthlyResults.get(Calendar.OCTOBER);
        MiniResultDto novemberResults = monthlyResults.get(Calendar.NOVEMBER);
        MiniResultDto decemberResults = monthlyResults.get(Calendar.DECEMBER);

        dataset.addValue(januaryResults.getExpenses().multiply(BigDecimal.valueOf(-1)), expenses, january);
        dataset.addValue(februaryResults.getExpenses().multiply(BigDecimal.valueOf(-1)), expenses, february);
        dataset.addValue(marchResults.getExpenses().multiply(BigDecimal.valueOf(-1)), expenses, march);
        dataset.addValue(aprilResults.getExpenses().multiply(BigDecimal.valueOf(-1)), expenses, april);
        dataset.addValue(mayResults.getExpenses().multiply(BigDecimal.valueOf(-1)), expenses, may);
        dataset.addValue(juneResults.getExpenses().multiply(BigDecimal.valueOf(-1)), expenses, june);
        dataset.addValue(julyResults.getExpenses().multiply(BigDecimal.valueOf(-1)), expenses, july);
        dataset.addValue(augustResults.getExpenses().multiply(BigDecimal.valueOf(-1)), expenses, august);
        dataset.addValue(septemberResults.getExpenses().multiply(BigDecimal.valueOf(-1)), expenses, september);
        dataset.addValue(octoberResults.getExpenses().multiply(BigDecimal.valueOf(-1)), expenses, october);
        dataset.addValue(novemberResults.getExpenses().multiply(BigDecimal.valueOf(-1)), expenses, november);
        dataset.addValue(decemberResults.getExpenses().multiply(BigDecimal.valueOf(-1)), expenses, december);

        dataset.addValue(januaryResults.getResult(), result, january);
        dataset.addValue(februaryResults.getResult(), result, february);
        dataset.addValue(marchResults.getResult(), result, march);
        dataset.addValue(aprilResults.getResult(), result, april);
        dataset.addValue(mayResults.getResult(), result, may);
        dataset.addValue(juneResults.getResult(), result, june);
        dataset.addValue(julyResults.getResult(), result, july);
        dataset.addValue(augustResults.getResult(), result, august);
        dataset.addValue(septemberResults.getResult(), result, september);
        dataset.addValue(octoberResults.getResult(), result, october);
        dataset.addValue(novemberResults.getResult(), result, november);
        dataset.addValue(decemberResults.getResult(), result, december);

        dataset.addValue(januaryResults.getIncomes(), incomes, january);
        dataset.addValue(februaryResults.getIncomes(), incomes, february);
        dataset.addValue(marchResults.getIncomes(), incomes, march);
        dataset.addValue(aprilResults.getIncomes(), incomes, april);
        dataset.addValue(mayResults.getIncomes(), incomes, may);
        dataset.addValue(juneResults.getIncomes(), incomes, june);
        dataset.addValue(julyResults.getIncomes(), incomes, july);
        dataset.addValue(augustResults.getIncomes(), incomes, august);
        dataset.addValue(septemberResults.getIncomes(), incomes, september);
        dataset.addValue(octoberResults.getIncomes(), incomes, october);
        dataset.addValue(novemberResults.getIncomes(), incomes, november);
        dataset.addValue(decemberResults.getIncomes(), incomes, december);

        return dataset;
    }

    public void setChartDataService(ChartDataService chartDataService) {
        this.chartDataService = chartDataService;
    }
}